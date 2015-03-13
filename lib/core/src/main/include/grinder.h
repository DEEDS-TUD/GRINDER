/*
 * grinder.h
 *
 *  Created on: Mar 9, 2012
 *      Author: Michael Tretter
 */

#ifndef GRINDER_H_
#define GRINDER_H_

#include <stddef.h>

struct victim {
	unsigned int interceptor_id; /* Creator of the Victim */
	unsigned char *location;     /* First byte of the injection location */
	size_t size;                 /* Size of the injection location */
};

typedef int (*injector_t)(const struct victim *);

injector_t get_injector(int id);

#endif /* GRINDER_H_ */
