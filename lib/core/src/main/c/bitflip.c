/*
 * bitflip.c
 *
 *  Created on: Apr 12, 2012
 *      Author: Michael Tretter
 */

#include "grinder.h"
#include "errormodels.h"

#include <stdlib.h>
#include <stdbool.h>

/*
 * The position of the bit to be flipped.
 */
static unsigned long testcase;

static bool configured = false;

static int inject(const struct victim *victim)
{
    unsigned long pos_byte;
    unsigned int pos_bit;
    unsigned char *flip;

    if (!configured)
        /* Do nothing, because error model is not configured. */
        return -1;

    if (testcase >= victim->size * 8) {
        /* Must not inject outside of specified memory area */
        return -1;
    }

    pos_byte = testcase / 8u;
    pos_bit  = (unsigned int)(testcase % 8u);

    flip = victim->location + pos_byte;
    *flip ^= (unsigned char)(1 << pos_bit);

    return 0;
}

static void configure(const char *configuration)
{
    testcase = strtoul(configuration, NULL, 10);
    configured = true;
}

struct error_model bitflip = {
    inject,
    configure,
    "Bitflip"
};
