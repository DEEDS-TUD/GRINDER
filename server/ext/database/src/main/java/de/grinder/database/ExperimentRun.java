package de.grinder.database;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.grinder.database.TestCase.KserviceNameToLongException;

@Entity
@Table(name = "experiment_run")
public class ExperimentRun {

    @Id
    @GeneratedValue
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @ManyToOne
    private TestCase testCase;

    @ManyToOne
    private Campaign campaign;

    @Column(columnDefinition = "SMALLINT")
    private short result;

    @Column(columnDefinition = "LONGTEXT")
    private String log;

    @Column(columnDefinition = "TEXT")
    private String activated;

    @Column(columnDefinition = "BIGINT")
    private long executionTime;

    public ExperimentRun() {
        super();
    }

    public ExperimentRun(final TestCase testCase, final Campaign campaign) {
        this();
        this.time = new Date();
        this.log = "";
        this.activated = "";
        this.testCase = testCase;
        this.campaign = campaign;
    }

    public int getId() {
        return id;
    }

    public Date getTime() {
        return time;
    }

    public String getLog() {
        return log;
    }

    public void appendLog(final String text) {
        log = log + text;
    }

    public TestCase getTestCase() {
        return testCase;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public String getConfiguration() {
        return testCase.getConfiguration();
    }

    public short getResult() {
        return result;
    }

    public void setResult(final short result) {
        this.result = result;
    }

    public void setStartExperiment() {
        executionTime = System.currentTimeMillis();
    }

    public void setEndExperiment() {
        executionTime = System.currentTimeMillis() - executionTime;
    }

    public byte[] getInjectionParameters() throws KserviceNameToLongException {
        return testCase.getInjectionParameters();
    }

    public void setActivated(final String activated) {
        this.activated = activated;
    }

    @Override
    public String toString() {
        return "Result [id=" + id + ", time=" + time + "]";
    }

    public void save() {
        final EntityManager em = Database.getEntityManager();

        em.getTransaction().begin();
        if (!em.contains(this)) {
            em.merge(this);
            em.flush();
        }
        em.getTransaction().commit();
        em.close();
    }
}