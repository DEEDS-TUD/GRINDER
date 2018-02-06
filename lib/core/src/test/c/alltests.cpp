/*
 * alltests.cpp
 *
 *  Created on: Sep 3, 2012
 *      Author: tretter
 */

#include <cppunit/TestSuite.h>
#include <cppunit/ui/text/TestRunner.h>
#include "bitfliptest.h"
#include "log_victimtest.h"

int main( int argc, char **argv)
{
    CppUnit::TextUi::TestRunner runner;
    runner.addTest(bitfliptest::suite());
    runner.addTest(log_victimtest::suite());
    bool wasSuccessful = runner.run();
    return wasSuccessful;
}
