/*
 * log_victimtest.cpp
 *
 *  Created on: Sep 17, 2012
 *      Author: Michael Tretter
 */

#include "log_victimtest.h"

#include <cstdlib>
#include <cppunit/TestAssert.h>

extern "C" {
#include "errormodels.h"
}

static int *foo;
static Victim *v0;

void log_victimtest::setUp() {
  foo = (int *) malloc(sizeof(int));
  *foo = 0;

  v0 = (Victim *) malloc(sizeof(Victim));
  v0->interceptor_id = 0;
  v0->location = (unsigned char *) foo;
  v0->size = 4;
}

void log_victimtest::tearDown() {
  free(foo);
  free(v0);
}

void log_victimtest::test() {
  logger.inject(v0);
  CPPUNIT_ASSERT_EQUAL(*foo, 0);
}
