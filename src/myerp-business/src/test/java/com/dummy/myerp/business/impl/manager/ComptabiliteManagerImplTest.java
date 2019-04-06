package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;


public class ComptabiliteManagerImplTest {

    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();

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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
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
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

}
