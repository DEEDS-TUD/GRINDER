package de.grinder.database;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * A campaign comprises multiple test cases.
 * 
 * 
 */
@Entity
@Table(name = "campaigns")
public class Campaign implements Iterable<TestCase> {

  @Id
  @GeneratedValue
  private Long id;

  @Column(length = 255)
  private String name;

  public String getName() {
    return name;
  }

  /**
   * Test cases that belong to this campaign.
   */
  @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
  private final List<TestCase> testCases;

  public Campaign() {
    super();
    this.testCases = new LinkedList<TestCase>();
  }

  public Long getId() {
    return id;
  }

  /**
   * Adds a {@link TestCase}.
   * 
   * @param testCase
   *          the test case to be added
   */
  public void add(final TestCase testCase) {
    testCases.add(testCase);
  }

  /**
   * Returns the {@link TestCase} with the given id.
   * 
   * @param id
   *          Id of the queried test case
   * @return Test case with the given id; null, if no test case was found
   */
  public TestCase get(final int id) {
    for (final TestCase testCase : testCases) {
      if (testCase.getId() == id) {
        return testCase;
      }
    }
    throw new NoSuchElementException();
  }

  @Override
  public String toString() {
    return "Campaign [id=" + id + " name=" + name + "]";
  }

  @Override
  public Iterator<TestCase> iterator() {
    return testCases.iterator();
  }

  /**
   * Tells the number of test cases
   * 
   * @return the number of test cases
   */
  public int size() {
    return testCases.size();
  }

  /**
   * Returns the test cases
   * 
   * @return the test cases
   */
  public List<TestCase> getTestCases() {
    return testCases;
  }
}
