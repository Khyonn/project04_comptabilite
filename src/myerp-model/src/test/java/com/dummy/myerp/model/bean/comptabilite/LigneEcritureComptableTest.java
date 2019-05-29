package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;

public class LigneEcritureComptableTest {
	
	@Test
	public void testGetterSetter() {
		LigneEcritureComptable lec = new LigneEcritureComptable();
		CompteComptable cc = new CompteComptable();
		
		
		lec.setCompteComptable(cc);
		lec.setLibelle("AnyLibelle");
		lec.setCredit(BigDecimal.valueOf(20.00));
		lec.setDebit(BigDecimal.valueOf(0.00));
		
		assertEquals(cc, lec.getCompteComptable());
		assertEquals("AnyLibelle", lec.getLibelle());
		assertEquals(0, lec.getCredit().compareTo(BigDecimal.valueOf(20.00)));
		assertEquals(0, lec.getDebit().compareTo(BigDecimal.valueOf(0.00)));
	}
}
