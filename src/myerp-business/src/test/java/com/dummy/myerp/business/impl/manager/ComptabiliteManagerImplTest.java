package com.dummy.myerp.business.impl.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.dummy.myerp.business.impl.BusinessProxyImpl;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class ComptabiliteManagerImplTest {

    private ComptabiliteManagerImpl comptabiliteManager;

    private ComptabiliteDao comptabiliteDao;
    private DaoProxy daoProxy;
    private TransactionManager transactionManager;

    @Before
    public void init() {
    	// comptabiliteDao
    	comptabiliteDao = Mockito.mock(ComptabiliteDaoImpl.class);
    	// daoProxy
    	daoProxy = Mockito.mock(DaoProxy.class);
    	Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
    	// transactionManager
    	transactionManager = Mockito.mock(TransactionManager.class);
    	
    	// comptabiliteManager
    	comptabiliteManager =  new ComptabiliteManagerImpl();
    	// configuration
    	ComptabiliteManagerImpl.configure(Mockito.mock(BusinessProxyImpl.class), daoProxy, transactionManager);
    }

    private EcritureComptable buildEcritureComptable(String libelle, String reference, Date date, JournalComptable journal) {
    	EcritureComptable ecritureComptable = new EcritureComptable();
    	
    	ecritureComptable.setLibelle(libelle);
    	ecritureComptable.setReference(reference);
    	ecritureComptable.setDate(date);
    	ecritureComptable.setJournal(journal);
    	return ecritureComptable;
    }
    
    // ========== VALID

    @Test
    public void checkEcritureComptableUnit() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, "CompteB"), "Credit X", null, new BigDecimal(123))
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }
    
    // ========== ERROR

    // ===== COMPTE COMPTABLE

    // === Numero null
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitNumeroCompteComptableNull() throws Exception {
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
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }

    // === Libelle null
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitLibelleCompteComptableNull() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2), "Credit X", null, new BigDecimal(123))
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }

    // === Libellé vide
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitLibelleCompteComptableEmpty() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, ""), "Credit X", null, new BigDecimal(123))
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }

    // === Libellé trop grand : > 150
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitLibelleCompteComptableTooLong() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(
        				new CompteComptable(2, "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"),
        				"Credit X",
        				null,
        				new BigDecimal(123)
    				)
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }
    
    // === null
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitCompteComptableNull() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(null, "Credit X", null, new BigDecimal(123))
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }
    
    // ===== LIBELLE
    
    // === null
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitLigneEcritureComptableLibelleNull() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, "CompteB"), null, null, new BigDecimal(123))
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }
    
    // === vide
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitLigneEcritureComptableLibelleEmpty() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, "CompteB"), "", null, new BigDecimal(123))
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }
    
    // === trop grand : > 200
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitLigneEcritureComptableLibelleTooLong() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(
        				new CompteComptable(2, "CompteB"),
        				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        				+ "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
        				null,
        				new BigDecimal(123)
    				)
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }
    
    // ===== DEBIT ET CREDIT

    // === Trois chiffres après la virgule 
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitLigneEcritureComptableCreditFractionThree() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable("AC", "Achat"));

        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123.001), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, "CompteB"), "Credit X", null, new BigDecimal(123.001))
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }
    
    // === 13 chiffres avant la virgule 
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitLigneEcritureComptableCreditIntegerThirteen() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(12345678901234.00), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, "CompteB"), "Credit X", null, new BigDecimal(12345678901234.00))
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }
    

    // ===== ECRITURE COMPTABLE

    // === Liste ligne écriture : taille < 2
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitListLigneEcritureTooSmall() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(12345678901234.00), new BigDecimal(12345678901234.00))
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }
    
    // === Libelle too long > 200
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitLibelleTooLong() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        		+ "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, "CompteB"), "Credit X", null, new BigDecimal(123))
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }
    
    // === Libelle null
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitLibelleNull() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		null,
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, "CompteB"), "Credit X", null, new BigDecimal(123))
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }
    
    // === Libelle empty
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitLibelleEmpty() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, "CompteB"), "Credit X", null, new BigDecimal(123))
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }
    
    // === Date null
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitDateNull() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		null,
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, "CompteB"), "Credit X", null, new BigDecimal(123))
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }
    
    // === Pattern de la référence non ok
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitReferencePattern() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/000001",
        		new Date(2019, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, "CompteB"), "Credit X", null, new BigDecimal(123))
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }
    
    // ===== JOURNAL COMPTABLE

    // === code null
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitJournalComptableCodeNull() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable(null, "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, "CompteB"), "Credit X", null, new BigDecimal(123))
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }
    
    // === code vide
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitJournalComptableCodeEmpty() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable("", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, "CompteB"), "Credit X", null, new BigDecimal(123))
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }
    
    // === code trop long : > 5
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitJournalComptableCodeTooLong() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable("AAAAAA", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, "CompteB"), "Credit X", null, new BigDecimal(123))
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }
    
    // === libellé null
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitJournalComptableLibelleNull() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable("AC", null));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, "CompteB"), "Credit X", null, new BigDecimal(123))
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }
    
    // === libellé vide
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitJournalComptableLibelleEmpty() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable("AC", ""));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, "CompteB"), "Credit X", null, new BigDecimal(123))
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }

    // === libellé trop long > 150
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitJournalComptableLibelleTooLong() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable("AC", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        				+ "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, "CompteB"), "Credit X", null, new BigDecimal(123))
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }
    
    // === Journal comptable null
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitJournalComptableNull() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		null);
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, "CompteB"), "Credit X", null, new BigDecimal(123))
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }
    
    // === Règle comptabilite 2 : les lignes écritures doivent s'équilibrer
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG2() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(124), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, "CompteB"), "Credit X", null, new BigDecimal(123))
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }
    
    // === Règle comptabilite 3 : il doit y avoir au moins une ligne au crédit et une ligne au débit
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG3() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), new BigDecimal(123))
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, "CompteB"), "Credit X", null, null)
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }
    
    // === Règle comptabilite 5 : Format et contenu de la référence
    // = La référence ne correspond pas au journal comptable
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG5Journal() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable("AB", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), new BigDecimal(123))
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, "CompteB"), "Credit X", null, null)
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }
    // = La référence ne correspond pas a l'année
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG5Annee() throws Exception {
        EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		new Date(2018, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), new BigDecimal(123))
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, "CompteB"), "Credit X", null, null)
		);
        comptabiliteManager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test
    public void testGetListCompteComptable() {
    	List<CompteComptable> ccList = new ArrayList<>();
    	
    	Mockito.when(comptabiliteDao.getListCompteComptable()).thenReturn(ccList);
    	assertSame(ccList, comptabiliteManager.getListCompteComptable());
    }
    
    @Test
    public void testGetListJournalComptable() {
    	List<JournalComptable> jcList = new ArrayList<>();
    	
    	Mockito.when(comptabiliteDao.getListJournalComptable()).thenReturn(jcList);
    	assertSame(jcList, comptabiliteManager.getListJournalComptable());
    }
    
    @Test
    public void testGetListEcritureComptable() {
    	List<EcritureComptable> ecList = new ArrayList<>();

    	Mockito.when(comptabiliteDao.getListEcritureComptable()).thenReturn(ecList);
    	assertSame(ecList, comptabiliteManager.getListEcritureComptable());
    }
    
    @Test 
    public void testAddReference() throws NotFoundException {
    	JournalComptable jc = new JournalComptable("AC", "Achat");
    	EcritureComptable ec = new EcritureComptable();
    	ec.setDate(new Date(2019, 1, 1));
    	ec.setJournal(jc);
    	
    	SequenceEcritureComptable sec = new SequenceEcritureComptable();
    	sec.setAnnee(2019);
    	sec.setDerniereValeur(1);
    	sec.setJournalComptable(jc);
    	
    	Mockito.when(comptabiliteDao.getSequenceEcritureComptableByJournalCodeAndAnnee("AC", 2019)).thenReturn(sec);
    	
    	comptabiliteManager.addReference(ec);
    	Mockito.verify(comptabiliteDao).updateSequenceEcritureComptable(sec);
    	assertEquals("AC-2019/00002", ec.getReference());
    }
    
    @Test
    public void testAddReferenceNew() throws NotFoundException {
    	JournalComptable jc = new JournalComptable("AC", "Achat");
    	EcritureComptable ec = new EcritureComptable();
    	ec.setDate(new Date(2019, 1, 1));
    	ec.setJournal(jc);
    	
    	Mockito.when(comptabiliteDao.getSequenceEcritureComptableByJournalCodeAndAnnee("AC", 2019)).thenThrow(NotFoundException.class);
    	
    	comptabiliteManager.addReference(ec);
    	assertEquals("AC-2019/00001", ec.getReference());
    }
    
    @Test
    public void testCheckEcritureComptableOK() throws NotFoundException, FunctionalException {
        // Ecriture comptable valide
    	EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, "CompteB"), "Credit X", null, new BigDecimal(123))
		);
        // Unicité de l'écriture comptable
        Mockito.when(comptabiliteDao.getEcritureComptableByRef("AC-2019/00001")).thenThrow(NotFoundException.class);
        
    	comptabiliteManager.checkEcritureComptable(vEcritureComptable);
    }
    
    @Test(expected = FunctionalException.class)
    public void testCheckEcritureComptableKONotValid() throws NotFoundException, FunctionalException {
        // Ecriture comptable NON valide
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
    public void testCheckEcritureComptableKONotUnique() throws NotFoundException, FunctionalException {
        // Ecriture comptable valide
    	EcritureComptable vEcritureComptable = buildEcritureComptable(
        		"Libelle",
        		"AC-2019/00001",
        		new Date(2019, 04, 05),
        		new JournalComptable("AC", "Achat"));
        
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(1, "CompteA"), "Debit X", new BigDecimal(123), null)
		);
        vEcritureComptable.getListLigneEcriture().add(
        		new LigneEcritureComptable(new CompteComptable(2, "CompteB"), "Credit X", null, new BigDecimal(123))
		);
        vEcritureComptable.setId(1);

        // NON unicité de l'écriture comptable
    	EcritureComptable vEcritureComptableB = buildEcritureComptable(
        		"LibelleB",
        		"AC-2019/00001",
        		null,
        		null);

    	vEcritureComptableB.setId(2);
        Mockito.when(comptabiliteDao.getEcritureComptableByRef("AC-2019/00001")).thenReturn(vEcritureComptableB);
        
    	comptabiliteManager.checkEcritureComptable(vEcritureComptable);
    }
}
