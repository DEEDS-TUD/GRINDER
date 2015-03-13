/*
 * tcpclient.c
 *
 *  Created on: Dec 13, 2011
 *      Author: Michael Tretter
 */

#include "tcpclient.h"

#include <arpa/inet.h>
#include <errno.h>
#include <limits.h>
#include <stddef.h>
#include <string.h>
#include <stdio.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>

static int sock;
static struct sockaddr_in serv_addr;

static int connect1(void)
{
	sock = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
	if (sock < 0) {
		fprintf(stderr, "Cannot open TCP socket. Error: %s\n",
			strerror(errno));
		return -1;
	}

	memset(&serv_addr, 0, sizeof(serv_addr));
	serv_addr.sin_family = AF_INET;
	serv_addr.sin_addr.s_addr = inet_addr(HOST);
	serv_addr.sin_port = htons(PORT);

	if (connect(sock,
		    (struct sockaddr *)&serv_addr, sizeof(serv_addr)) < 0) {
		fprintf(stderr, "Cannot connect to GRINDER server. Error: %s\n",
			strerror(errno));
		return -1;
	}

	return 0;
}

static int check_socket(void)
{
	return sock != 0 ? 0 : connect1();
}

int send_bytes(const char *buffer, size_t size)
{
	ssize_t sent;

	if (!check_socket()) {
		return -1;
	}

	sent = send(sock, buffer, size, 0);
	if (sent > 0 && sent != size) {
		/* FIXME: is this a bug?
		 * Shall the caller handle this? */
		return -1;
	}

	return sent == size ? 0 : errno;
}

int recv_bytes(char *buffer, size_t size)
{
	ssize_t received;

	if (!check_socket())
		return -1;

	received = recv(sock, buffer, size, 0);

	if (received < 0)
		return errno;

	if (received > INT_MAX)
		return -EOVERFLOW;

	return (int)received;
}
