/*
 * bitfliptest.h
 *
 *  Created on: Sep 3, 2012
 *      Author: tretter
 */

#ifndef BITFLIPTEST_H_
#define BITFLIPTEST_H_

#include <cppunit/TestFixture.h>
#include <cppunit/TestCaller.h>
#include <cppunit/TestSuite.h>

class bitfliptest: public CppUnit::TestFixture {
public:
  static CppUnit::Test *suite() {
    CppUnit::TestSuite *suite = new CppUnit::TestSuite("bitfliptest");

    suite->addTest(
        new CppUnit::TestCaller<bitfliptest>("Test injection without configuring injector", &bitfliptest::testNotConfigured));
    suite->addTest(
        new CppUnit::TestCaller<bitfliptest>("Test injection into least significant bit", &bitfliptest::testLeastSignificantBitInjection));
    suite->addTest(
        new CppUnit::TestCaller<bitfliptest>("Test injection into most significant bit", &bitfliptest::testMostSignificantBitInjection));
    suite->addTest(
        new CppUnit::TestCaller<bitfliptest>("Test injection into arbitrary legal bit", &bitfliptest::testSomeBitInjection));
    suite->addTest(
        new CppUnit::TestCaller<bitfliptest>("Test injection into bit with index higher than size of victim", &bitfliptest::testIndexToHighInjection));
    suite->addTest(
        new CppUnit::TestCaller<bitfliptest>("Test injection into bit with negative index", &bitfliptest::testInjectionWithNegativeIndex));
    return suite;
  }

  void setUp();
  void tearDown();

private:
  void testNotConfigured();
  void testLeastSignificantBitInjection();
  void testMostSignificantBitInjection();
  void testSomeBitInjection();
  void testIndexToHighInjection();
  void testInjectionWithNegativeIndex();
};

#endif /* BITFLIPTEST_H_ */
