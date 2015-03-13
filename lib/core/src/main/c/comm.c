/*
 * comm.c
 *
 *  Created on: Feb 13, 2012
 *      Author: Michael Tretter
 */

#include "comm.h"

#include "tcpclient.h"
#include <limits.h>
#include <stddef.h>
#include <stdio.h>
#include <string.h>
#include <arpa/inet.h>

/* see de.grinder.util.message.MessageTypes (Java) */
#define TYPE_GET_CONFIGURATION  1
#define TYPE_LOG                3
#define TYPE_SEND_CONFIGURATION 4

#define CHECKED_TCP_CALL(fun, ...)					       \
	do {								       \
		int error = fun(__VA_ARGS__);				       \
		if(error < 0) {						       \
			fprintf(stderr, #fun " returned with an error: %d.\n", \
				error);					       \
			return -1;					       \
		}							       \
	} while(0)

struct network_header {
	short type;
	short runid;
	short size;
} __packed;

struct header {
	short type;
	short runid;
	short size;
};

static short htonss(short host)
{
	uint16_t net = htons(*(uint16_t *)&host);
	return *(short *)&net;
}

static short ntohss(short net)
{
	uint16_t host = ntohs(*(uint16_t *)&net);
	return *(short *)&host;
}

static int send_header(const struct header *header)
{
	struct network_header network_header = {
		htonss(header->type ),
		htonss(header->runid),
		htonss(header->size )
	};

	CHECKED_TCP_CALL(send_bytes, (char *)&network_header,
			 sizeof(network_header));

	return 0;
}

static int recv_header(struct header *header)
{
	struct network_header network_header;
	int recv;

	CHECKED_TCP_CALL(recv = recv_bytes, (char *)&network_header,
			 sizeof(network_header));

	if (recv != sizeof(network_header)) {
		fprintf(stderr, "recv_header: header too short.\n");
		return -1;
	}

	header->type = ntohss(network_header.type  );
	header->runid = ntohss(network_header.runid);
	header->size = ntohss(network_header.size  );

	return 0;
}

int send_log(const char *message)
{
	size_t length = strlen(message);
	struct header header = {
		TYPE_LOG,
		0,
		(short)length
	};

	if (length > SHRT_MAX) {
		fprintf(stderr, "message '%s' longer than %d.\n", message,
			SHRT_MAX);
		return -1;
	}

	/* send message */
	if (send_header(&header))
		return -1;
	CHECKED_TCP_CALL(send_bytes, message, length);

	return 0;
}

int get_configuration(char *configuration, size_t size)
{
	struct header header = {
		TYPE_GET_CONFIGURATION,
		0,
		0
	};
	int recv;

	/* send request */
	if (send_header(&header))
		return -1;

	/* receive header of answer */
	if (recv_header(&header))
		return -1;

	/* sanity checks */
	if (header.type != TYPE_SEND_CONFIGURATION) {
		fprintf(stderr,
			"Unexpected message type %d received while requesting injection parameters.\n",
			header.type);
		return -1;
	}
	if (header.size < 0) {
		fprintf(stderr, "Message size is %d.\n", header.size);
		return -1;
	}
	if (header.size > size) {
		fprintf(stderr,
			"Message size %d > configuration buffer size %zd.\n",
			header.size, size);
		return -1;
	}

	/* receive configuration */
	CHECKED_TCP_CALL(recv = recv_bytes, configuration, (size_t)header.size);
	if (recv < header.size)
		fprintf(stderr,
			"Warning: Received configuration shorter than announced.\n");

	return 0;
}
