package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Test;
import static org.junit.Assert.*;

public class SequenceEcritureComptableTest {

	@Test
	public void testConstructor() {
		SequenceEcritureComptable sec = new SequenceEcritureComptable();
		
		assertNull(sec.getAnnee());
		assertNull(sec.getDerniereValeur());
		assertNull(sec.getJournalComptable());
	}
	
	@Test
	public void testConstructorWithArgs() {
		SequenceEcritureComptable sec = new SequenceEcritureComptable(1995, 1);
		
		assertEquals(1995, sec.getAnnee().intValue());
		assertEquals(1, sec.getDerniereValeur().intValue());
		assertNull(sec.getJournalComptable());
	}
	
	@Test
	public void testGetterSetter() {
		SequenceEcritureComptable sec = new SequenceEcritureComptable();
		JournalComptable jc = new JournalComptable();
		
		
		sec.setAnnee(1995);
		sec.setDerniereValeur(1);
		sec.setJournalComptable(jc);

		assertEquals(1995, sec.getAnnee().intValue());
		assertEquals(1, sec.getDerniereValeur().intValue());
		assertSame(jc, sec.getJournalComptable());
	}
	
	@Test
	public void testToString() {
		SequenceEcritureComptable sec = new SequenceEcritureComptable(1995, 1);
		
		assertEquals("SequenceEcritureComptable{annee=1995, derniereValeur=1}", sec.toString());
	}
}
