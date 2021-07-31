package com.iktakademija.eDnevnik.services;

import java.util.List;

import com.iktakademija.eDnevnik.entities.NastavnikEntity;
import com.iktakademija.eDnevnik.entities.OcenaEntity;
import com.iktakademija.eDnevnik.entities.OdeljenjeEntity;

public interface OcenaDAO {

	List<OcenaEntity> findOcenaByNastavnikAndOdeljenje(NastavnikEntity nastavnikId, OdeljenjeEntity odeljenjeId);
}
