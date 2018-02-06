/*
 * log_victim.c
 *
 *  Created on: Apr 12, 2012
 *      Author: Michael Tretter
 */

#include "errormodels.h"
#include "comm.h"

#include <stdio.h>

/* Logging */
#define BUFSIZE 512

static char message[BUFSIZE];

static int inject(const struct victim *victim)
{
    /* Format the log message and send it to the server. */
    sprintf(message, "interceptor: %d\n  logged_value: %.*llx\n",
            victim->interceptor_id,
            (int)victim->size, *(unsigned long long *)victim->location);
    send_log(message);

    return 0;
}

static void configure(const char *configuration)
{
    /* Currently no configuration possible */
    return;
}

struct error_model logger = {
    inject,
    configure,
    "Logger"
};
