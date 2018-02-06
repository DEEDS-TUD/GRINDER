/*
 * bitfliptest.cpp
 *
 *  Created on: Aug 27, 2012
 *      Author: Michael Tretter
 */

#include "bitfliptest.h"

#include <cstdlib>
#include <cppunit/TestAssert.h>

extern "C" {
#include "errormodels.h"
}

static int *foo;
static Victim *v0;

void bitfliptest::setUp() {
    foo = (int *) malloc(sizeof(int));
    *foo = 0;

    v0 = (Victim *) malloc(sizeof(Victim));
    v0->interceptor_id = 0;
    v0->location = (unsigned char *) foo;
    v0->size = 4;
}

void bitfliptest::tearDown() {
    free(foo);
    free(v0);
}

void bitfliptest::testNotConfigured() {
    bitflip.inject(v0);
    CPPUNIT_ASSERT_EQUAL(0, *foo);
}

void bitfliptest::testLeastSignificantBitInjection() {
    bitflip.configure((char *) "0");
    bitflip.inject(v0);
    CPPUNIT_ASSERT_EQUAL(1, *foo);
}

void bitfliptest::testMostSignificantBitInjection() {
    bitflip.configure((char *) "31");
    bitflip.inject(v0);
    CPPUNIT_ASSERT_EQUAL((int) 0x80000000, *foo);
}

void bitfliptest::testSomeBitInjection() {
    bitflip.configure((char *) "25");
    bitflip.inject(v0);
    CPPUNIT_ASSERT_EQUAL(33554432, *foo);
}

void bitfliptest::testIndexToHighInjection() {
    bitflip.configure((char *) "32");
    bitflip.inject(v0);
    CPPUNIT_ASSERT_EQUAL(0, *foo);
}

void bitfliptest::testInjectionWithNegativeIndex() {
    bitflip.configure((char *) "-1");
    bitflip.inject(v0);
    CPPUNIT_ASSERT_EQUAL(0, *foo);
}
