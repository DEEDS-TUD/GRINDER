/*
 * log_victimtest.h
 *
 *  Created on: Sep 17, 2012
 *      Author: tretter
 */

#ifndef LOG_VICTIMTEST_H_
#define LOG_VICTIMTEST_H_

#include <cppunit/TestFixture.h>
#include <cppunit/TestCaller.h>
#include <cppunit/TestSuite.h>

class log_victimtest: public CppUnit::TestFixture {
public:
  static CppUnit::Test *suite() {
    CppUnit::TestSuite *suite = new CppUnit::TestSuite("log_victimtest");

    suite->addTest(
        new CppUnit::TestCaller<log_victimtest>("Test", &log_victimtest::test));
    return suite;
  }

  void setUp();
  void tearDown();

private:
  void test();
};

#endif /* LOG_VICTIMTEST_H_ */
