package com.dummy.myerp.testbusiness.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.annotation.Order;

import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;

public class ComptabiliteManagerTest extends BusinessTestCase {
	
	private ComptabiliteManager comptabiliteManager;

    private EcritureComptable buildEcritureComptable(String libelle, String reference, Date date, JournalComptable journal) {
    	EcritureComptable ecritureComptable = new EcritureComptable();
    	
    	ecritureComptable.setLibelle(libelle);
    	ecritureComptable.setReference(reference);
    	ecritureComptable.setDate(date);
    	ecritureComptable.setJournal(journal);
    	return ecritureComptable;
    }
	
	@Before
	public void init() {
		comptabiliteManager = getBusinessProxy().getComptabiliteManager();
	}
	
	@Test
	public void testGetListCompteComptable() {
		List<CompteComptable> ccList = comptabiliteManager.getListCompteComptable();
		assertEquals(7, ccList.size());
	}
	
	@Test
	public void testGetListJournalComptable() {
		List<JournalComptable> ccList = comptabiliteManager.getListJournalComptable();
		assertEquals(4, ccList.size());
	}
	
	@Test
	public void testGetListEcritureComptable() {
		List<EcritureComptable> ccList = comptabiliteManager.getListEcritureComptable();
		assertEquals(6, ccList.size());
	}
	
	@Test
	public void testAddReferenceOKNewSequence() throws FunctionalException {
		JournalComptable jc = new JournalComptable("AC", "Achat");
		EcritureComptable ec = new EcritureComptable();
		Date d = new Date(2018, 01, 01);
		ec.setJournal(jc);
		ec.setDate(d);
		ec.setLibelle("Premiere EcritureComptable pour 2018");
		
		comptabiliteManager.addReference(ec);
		assertEquals("AC-2018/00001", ec.getReference());
	}
	
	@Test
	public void testAddReferenceOKExistingSequence() throws FunctionalException {
		JournalComptable jc = new JournalComptable("AC", "Achat");
		EcritureComptable ec = new EcritureComptable();
		Date d = new Date(2016, 01, 01);
		ec.setJournal(jc);
		ec.setDate(d);
		ec.setLibelle("41eme EcritureComptable pour 2016");
		
		comptabiliteManager.addReference(ec);
		assertEquals("AC-2016/00041", ec.getReference());
	}
	
	@Test(expected = NullPointerException.class)
	public void testAddReferenceKOEcritureNull() throws FunctionalException {
		comptabiliteManager.addReference(null);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAddReferenceKOEcritureJournalNull() throws FunctionalException {
		EcritureComptable ec = new EcritureComptable();
		ec.setDate(new Date());
		comptabiliteManager.addReference(ec);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAddReferenceKOEcritureDateNull() throws FunctionalException {
		EcritureComptable ec = new EcritureComptable();
		ec.setJournal(new JournalComptable("AC", "Achat"));
		comptabiliteManager.addReference(ec);
	}
	
	@Test
	public void testCheckEcritureComptableOK() throws FunctionalException {
		// Ici pas de assert : si aucune exception n'est levée, l'Ecriture est OK
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2016/00042",
        		new Date(2016, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(411, "Client"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(401, "Fournisseurs"), "Credit X", null, new BigDecimal(123))
		);
		comptabiliteManager.checkEcritureComptable(vEcritureComptable);
	}
	
	@Test(expected = FunctionalException.class)
	public void testCheckEcritureComptableKOConstraint() throws FunctionalException {
		// L'id du compte comptable n'est pas renseigné ici
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(null, "CompteB"), "Credit X", null, new BigDecimal(123))
		);
		comptabiliteManager.checkEcritureComptable(vEcritureComptable);
	}
	
	@Test(expected = FunctionalException.class)
	public void testCheckEcritureComptableKOEquilibree() throws FunctionalException {
		// Les lignes ne sont pas équilibrée (debit - credit != 0)
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, "CompteB"), "Credit X", null, new BigDecimal(124))
		);
		comptabiliteManager.checkEcritureComptable(vEcritureComptable);
	}
	
	@Test(expected = FunctionalException.class)
	public void testCheckEcritureComptableKOExisting() throws FunctionalException {
		// Il existe déjà une même référence
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2016/00001",
        		new Date(2016, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, "CompteB"), "Credit X", null, new BigDecimal(123))
		);
		comptabiliteManager.checkEcritureComptable(vEcritureComptable);
	}
	
	@Test
	public void testInsertEcritureComptableOK() throws FunctionalException {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Une ecriture de test",
        		"AC-2016/00042",
        		new Date(2016, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(411, "Client"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(401, "Fournisseurs"), "Credit X", null, new BigDecimal(123))
		);
		comptabiliteManager.insertEcritureComptable(vEcritureComptable);
		assertNotNull(vEcritureComptable.getId());
	}
	
	@Test(expected = FunctionalException.class)
	public void testInsertEcritureComptableKOChecks() throws FunctionalException {
		// Il existe déjà une même référence
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2016/00001",
        		new Date(2016, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, "CompteB"), "Credit X", null, new BigDecimal(123))
		);
		comptabiliteManager.insertEcritureComptable(vEcritureComptable);
	}
	
	@Test
	public void testUpdateEcritureComptableOK() throws FunctionalException {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2016/00001",
        		new Date(2016, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(411, "Client"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(401, "Fournisseurs"), "Credit X", null, new BigDecimal(123))
		);
        vEcritureComptable.setId(-1);
		comptabiliteManager.updateEcritureComptable(vEcritureComptable);
	}
	
	@Test(expected = FunctionalException.class)
	public void testUpdateEcritureComptableKODifferentId() throws FunctionalException {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2016/00001",
        		new Date(2016, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(411, "Client"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(401, "Fournisseurs"), "Credit X", null, new BigDecimal(123))
		);
        // Pour la référence donnée, l'id de l'écriture comptable ne correspond pas
        vEcritureComptable.setId(-2);
		comptabiliteManager.updateEcritureComptable(vEcritureComptable);
	}
	
	private EcritureComptable findEcritureComptableById(Integer id) {
		return comptabiliteManager.getListEcritureComptable().stream()
				.filter(ec -> ec.getId().equals(id))
				.findFirst().orElse(null);
	}

	@Test
	public void testLastDeleteEcritureComptableOK() throws FunctionalException {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Une ecriture de test",
        		"AC-2016/00042",
        		new Date(2016, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(411, "Client"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(401, "Fournisseurs"), "Credit X", null, new BigDecimal(123))
		);
		comptabiliteManager.insertEcritureComptable(vEcritureComptable);
		assertNotNull(vEcritureComptable.getId());

		EcritureComptable ecBefore = findEcritureComptableById(vEcritureComptable.getId());
		comptabiliteManager.deleteEcritureComptable(vEcritureComptable.getId());
		EcritureComptable ecAfter = findEcritureComptableById(vEcritureComptable.getId());

		assertNotNull(ecBefore);
		assertNull(ecAfter);
	}
}