package com.dummy.myerp.model.bean.comptabilite;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.Assert;

public class CompteComptableTest {
	
	private List<CompteComptable> getList(){
		List<CompteComptable> compteList = new ArrayList<>();

		compteList.add(new CompteComptable(null, "Compte A"));
		compteList.add(new CompteComptable(1, "Compte B"));
		compteList.add(null);
		return compteList;
	}

	@Test
	public void testGetByNumeroNull() {
		Assert.assertEquals("Compte A", CompteComptable.getByNumero(this.getList(), null).getLibelle());
	}

	@Test
	public void testGetByNumeroNotNull() {
		Assert.assertEquals("Compte B", CompteComptable.getByNumero(this.getList(), 1).getLibelle());
	}

	@Test
	public void testGetByNumeroNotFound() {
		Assert.assertNull(CompteComptable.getByNumero(this.getList(), 2));
	}
	
	@Test
	public void testGetterSetter() {
		CompteComptable cc = new CompteComptable();
		cc.setLibelle("AnyLibelle");
		cc.setNumero(1);
		
		Assert.assertEquals("AnyLibelle", cc.getLibelle());
		Assert.assertEquals(1, cc.getNumero().intValue());
	}
}
