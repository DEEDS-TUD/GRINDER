/*
 * grinder.c
 *
 *  Created on: Sep 5, 2012
 *      Author: Michael Tretter
 */

#include "grinder.h"

#include "comm.h"
#include "errormodels.h"
#include <string.h>
#include <stdio.h>
#include <stdbool.h>

/* TODO: Make current restriction of the configuration size to 255 bytes configurable and add proper documentation */
static const int CONFIG_SIZE = 255;

/*
 * Data structure for storing the mapping of interceptors and injectors.
 * Currently, we use only a very simple mapping using a array and its indices,
 * but as the data structure is hidden behind get_injector, it can change
 * without affecting existing interceptors.
 */
static const struct error_model *errormodels[] = { &bitflip, &logger };

static bool configured = false;

static void setup(int id)
{
	char configuration[CONFIG_SIZE];
	get_configuration(configuration, sizeof(configuration));

	errormodels[id]->configure(configuration);
	configured = true;
}

injector_t get_injector(int id)
{
	if (!configured)
		setup(id);

	return errormodels[id]->inject;
}
