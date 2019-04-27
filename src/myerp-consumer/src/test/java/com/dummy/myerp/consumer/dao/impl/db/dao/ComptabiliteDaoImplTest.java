package com.dummy.myerp.consumer.dao.impl.db.dao;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.NotFoundException;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/dummy/myerp/consumer/applicationContext.xml")
public class ComptabiliteDaoImplTest {
	// === Taille des listes
	private static final int COMPTE_COMPTABLE_NB = 7;
	private static final int ECRITURE_COMPTABLE_NB = 5;
	private static final int JOURNAL_COMPTABLE_NB = 4;
	private static final int EC_LIGNE_ECRITURE_NB = 3;
	
	// === Ecriture comptable ID
	private static final int EC_ID_OK = -1;
	private static final int EC_ID_KO = 2;
	private static final Integer EC_ID_NEW = 1;
	// === Ecriture comptable REF
	private static final String EC_REF_OK = "AC-2016/00001";
	private static final String EC_REF_KO = StringUtils.EMPTY;
	private static final Integer YEAR = 2016;
	private static final Integer SEQ_EC_LAST_VALUE = 40;
	private static final Integer NEW_YEAR = 2017;
	private static final Integer NEW_SEQ_EC_LAST_VALUE = 0;
	// === JOURNAL
	private static final String JOURNAL_CODE = "AC";
	private static final String JOURNAL_LIBELLE = "Achat";
	
	private ComptabiliteDao comptabiliteDao = ComptabiliteDaoImpl.getInstance();
	
	@Test
	public void testGetListCompteComptable() {
		Assert.assertEquals(COMPTE_COMPTABLE_NB, comptabiliteDao.getListCompteComptable().size());
	}
	
	@Test
	public void testGetListEcritureComptable() {
		Assert.assertEquals(ECRITURE_COMPTABLE_NB, comptabiliteDao.getListEcritureComptable().size());
	}
	
	@Test
	public void testGetListJournalComptable() {
		Assert.assertEquals(JOURNAL_COMPTABLE_NB, comptabiliteDao.getListJournalComptable().size());
	}
	
	@Test
	public void testGetEcritureComptableOK() throws Exception  {
		Assert.assertEquals(JOURNAL_LIBELLE, comptabiliteDao.getEcritureComptable(EC_ID_OK).getJournal().getLibelle());
	}
	
	@Test(expected = NotFoundException.class)
	public void testGetEcritureComptableKONotFound() throws Exception  {
		comptabiliteDao.getEcritureComptable(EC_ID_KO);
	}
	
	@Test
	public void testGetEcritureComptableByRef() throws Exception {
		Assert.assertEquals(JOURNAL_LIBELLE, comptabiliteDao.getEcritureComptableByRef(EC_REF_OK).getJournal().getLibelle());
	}
	
	@Test(expected = NotFoundException.class)
	public void testGetEcritureComptableByRefKONotFound() throws Exception  {
		comptabiliteDao.getEcritureComptableByRef(EC_REF_KO);
	}
	
	@Test
	public void testLoadListLigneEcriture() {
		EcritureComptable ec = new EcritureComptable();
		ec.setId(EC_ID_OK);
		comptabiliteDao.loadListLigneEcriture(ec);
		Assert.assertEquals(EC_LIGNE_ECRITURE_NB, ec.getListLigneEcriture().size());
	}
	
	@Test
	public void testInsertEcritureComptable() {
		EcritureComptable ec = new EcritureComptable();
		Date date = new Date();
		ec.setJournal(new JournalComptable(JOURNAL_CODE, JOURNAL_LIBELLE));
		ec.setDate(date);
		ec.setReference(JOURNAL_CODE + "-" + date.getYear() + "/00001");
		ec.setLibelle("Plop");
		
		comptabiliteDao.insertEcritureComptable(ec);
		Assert.assertEquals(EC_ID_NEW, ec.getId());
	}
	
	@Test
	public void testUpdateEcritureComptable() {
		EcritureComptable ec = new EcritureComptable();
		Date date = new Date();
		ec.setId(EC_ID_NEW);
		ec.setJournal(new JournalComptable(JOURNAL_CODE, JOURNAL_LIBELLE));
		ec.setDate(date);
		ec.setReference(JOURNAL_CODE + "-" + date.getYear() + "/00001");
		ec.setLibelle("Plap");
		
		comptabiliteDao.updateEcritureComptable(ec);
		try {
			Assert.assertEquals("Plap", comptabiliteDao.getEcritureComptable(EC_ID_NEW).getLibelle());
		} catch (NotFoundException e) {
			Assert.fail();
		}
	}
	
	@Test(expected = NotFoundException.class)
	public void testDeleteEcritureComptable() throws Exception {
		comptabiliteDao.deleteEcritureComptable(EC_ID_NEW);
		comptabiliteDao.getEcritureComptable(EC_ID_NEW); // Cette méthode va throw une NotFoundException
	}
	
	@Test
	public void testGetSequenceEcritureComptableByJournalCodeAndAnnee() throws Exception {
		SequenceEcritureComptable sec = comptabiliteDao.getSequenceEcritureComptableByJournalCodeAndAnnee(JOURNAL_CODE, YEAR);
		Assert.assertEquals(SEQ_EC_LAST_VALUE, sec.getDerniereValeur());
	}
	
	@Test
	public void testGetJournalComptableByCode() throws Exception {
		JournalComptable jc = comptabiliteDao.getJournalComptableByCode(JOURNAL_CODE);
		Assert.assertEquals(JOURNAL_LIBELLE, jc.getLibelle());
	}
	
	@Test
	public void testCreateSequenceEcritureComptable() {
		SequenceEcritureComptable sec = new SequenceEcritureComptable();
		sec.setDerniereValeur(NEW_SEQ_EC_LAST_VALUE);
		sec.setJournalComptable(new JournalComptable(JOURNAL_CODE, JOURNAL_LIBELLE));
		sec.setAnnee(NEW_YEAR);
		comptabiliteDao.createSequenceEcritureComptable(sec);
		try {
			Assert.assertEquals(NEW_SEQ_EC_LAST_VALUE, comptabiliteDao.getSequenceEcritureComptableByJournalCodeAndAnnee(JOURNAL_CODE, NEW_YEAR).getDerniereValeur());
		} catch (NotFoundException e) {
			Assert.fail();
		}
	}
	
	@Test
	public void testUpdateSequenceEcritureComptable() {
		final Integer UPDATED_VALUE = NEW_SEQ_EC_LAST_VALUE + 1; 
		SequenceEcritureComptable sec = new SequenceEcritureComptable();
		sec.setDerniereValeur(UPDATED_VALUE);
		sec.setJournalComptable(new JournalComptable(JOURNAL_CODE, JOURNAL_LIBELLE));
		sec.setAnnee(NEW_YEAR);

		comptabiliteDao.updateSequenceEcritureComptable(sec);
		try {
			Assert.assertEquals(UPDATED_VALUE, comptabiliteDao.getSequenceEcritureComptableByJournalCodeAndAnnee(JOURNAL_CODE, NEW_YEAR).getDerniereValeur());
		} catch (NotFoundException e) {
			Assert.fail();
		}
	}
}