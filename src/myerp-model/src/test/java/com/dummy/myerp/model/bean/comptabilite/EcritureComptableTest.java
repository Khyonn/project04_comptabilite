package com.dummy.myerp.model.bean.comptabilite;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Assert;
import org.junit.Test;

public class EcritureComptableTest {

	private LigneEcritureComptable createLigne(Integer pCompteComptableNumero, String pDebit, String pCredit) {
		BigDecimal vDebit = pDebit == null ? null : new BigDecimal(pDebit);
		BigDecimal vCredit = pCredit == null ? null : new BigDecimal(pCredit);
		String vLibelle = ObjectUtils.defaultIfNull(vDebit, BigDecimal.ZERO)
				.subtract(ObjectUtils.defaultIfNull(vCredit, BigDecimal.ZERO)).toPlainString();
		LigneEcritureComptable vRetour = new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero),
				vLibelle, vDebit, vCredit);
		return vRetour;
	}

	// ===== RG_Compta_2
	@Test
	public void isEquilibree() {
		EcritureComptable vEcriture;
		vEcriture = new EcritureComptable();

		vEcriture.setLibelle("Equilibrée");
		vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
		vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
		vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "301"));
		vEcriture.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
		Assert.assertTrue(vEcriture.toString(), vEcriture.isEquilibree());

		vEcriture.getListLigneEcriture().clear();
		vEcriture.setLibelle("Non équilibrée");
		vEcriture.getListLigneEcriture().add(this.createLigne(1, "10", null));
		vEcriture.getListLigneEcriture().add(this.createLigne(1, "20", "1"));
		vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "30"));
		vEcriture.getListLigneEcriture().add(this.createLigne(2, "1", "2"));
		Assert.assertFalse(vEcriture.toString(), vEcriture.isEquilibree());
	}
	
	@Test
	public void testTotalDebit() {
		EcritureComptable vEcriture;
		vEcriture = new EcritureComptable();
		LigneEcritureComptable vLigneEcritureComptable = this.createLigne(1, null, null);
		vLigneEcritureComptable.setDebit(null);

		vEcriture.getListLigneEcriture().add(this.createLigne(1, "150.00", "250"));
		vEcriture.getListLigneEcriture().add(this.createLigne(1, "350", null));
		vEcriture.getListLigneEcriture().add(vLigneEcritureComptable);
		Assert.assertEquals(new BigDecimal("500.00"), vEcriture.getTotalDebit());
	}
	
	@Test
	public void testTotalCredit() {
		EcritureComptable vEcriture;
		vEcriture = new EcritureComptable();
		LigneEcritureComptable vLigneEcritureComptable = this.createLigne(1, null, null);
		vLigneEcritureComptable.setCredit(null);

		vEcriture.getListLigneEcriture().add(this.createLigne(1, "250", "150.00"));
		vEcriture.getListLigneEcriture().add(this.createLigne(1, null, "350"));
		vEcriture.getListLigneEcriture().add(vLigneEcritureComptable);
		Assert.assertEquals(new BigDecimal("500.00"), vEcriture.getTotalCredit());
	}
	
	@Test
	public void testGetterSetter() {
		EcritureComptable ec = new EcritureComptable();
		JournalComptable jc = new JournalComptable();
		Date d = new Date();
		
		ec.setDate(d);
		ec.setId(1);
		ec.setJournal(jc);
		ec.setLibelle("AnyLibelle");
		ec.setReference("AnyReference");
		
		Assert.assertSame(d, ec.getDate());
		Assert.assertSame(jc, ec.getJournal());
		Assert.assertEquals(1, ec.getId().intValue());
		Assert.assertEquals("AnyLibelle", ec.getLibelle());
		Assert.assertEquals("AnyReference", ec.getReference());
	}
}
