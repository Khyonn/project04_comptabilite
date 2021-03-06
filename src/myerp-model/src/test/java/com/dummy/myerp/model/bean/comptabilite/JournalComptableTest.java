package com.dummy.myerp.model.bean.comptabilite;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class JournalComptableTest {

	private List<JournalComptable> getList(){
		List<JournalComptable> compteList = new ArrayList<>();

		compteList.add(new JournalComptable(null, "Journal A"));
		compteList.add(new JournalComptable("B", "Journal B"));
		compteList.add(null);
		return compteList;
	}

	@Test
	public void testGetByCodeNull() {
		Assert.assertEquals("Journal A", JournalComptable.getByCode(this.getList(), null).getLibelle());
	}

	@Test
	public void testGetByCodeNotNull() {
		Assert.assertEquals("Journal B", JournalComptable.getByCode(this.getList(), "B").getLibelle());
	}

	@Test
	public void testGetByCodeNotFound() {
		Assert.assertNull(JournalComptable.getByCode(this.getList(), "A"));
	}
	
	@Test
	public void testDefaultConstructor() {
		JournalComptable jc = new JournalComptable();

		Assert.assertNull(jc.getCode());
		Assert.assertNull(jc.getLibelle());
	}
	
	@Test
	public void testToString() {
		JournalComptable jc = new JournalComptable();
		jc.setCode("AnyCode");
		jc.setLibelle("AnyLibelle");
		
		Assert.assertEquals("JournalComptable{code='AnyCode', libelle='AnyLibelle'}", jc.toString());
	}
}
