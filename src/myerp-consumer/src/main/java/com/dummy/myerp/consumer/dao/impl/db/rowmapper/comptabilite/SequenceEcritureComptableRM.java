package com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.dummy.myerp.consumer.ConsumerHelper;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.NotFoundException;


/**
 * {@link RowMapper} de {@link CompteComptable}
 */
public class SequenceEcritureComptableRM implements RowMapper<SequenceEcritureComptable> {

    @Override
    public SequenceEcritureComptable mapRow(ResultSet pRS, int pRowNum) throws SQLException {
    	SequenceEcritureComptable vBean = new SequenceEcritureComptable();
        vBean.setAnnee(pRS.getInt("annee"));
        vBean.setDerniereValeur(pRS.getInt("derniere_valeur"));
        
        JournalComptable journal;
        try {
			journal = ConsumerHelper.getDaoProxy().getComptabiliteDao().getJournalComptableByCode(pRS.getString("journal_code"));
		} catch (NotFoundException e) {
			journal = null;
		}
        vBean.setJournalComptable(journal);

        return vBean;
    }
}
