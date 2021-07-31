package com.iktakademija.eDnevnik.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.iktakademija.eDnevnik.entities.NastavnikEntity;
import com.iktakademija.eDnevnik.entities.OcenaEntity;
import com.iktakademija.eDnevnik.entities.OdeljenjeEntity;
@Service
public class OcenaDAOImpl implements OcenaDAO {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	
	public List<OcenaEntity> findOcenaByNastavnikAndOdeljenje(NastavnikEntity nastavnik, OdeljenjeEntity odeljenje) {
		// select * from ocena o left join ucenik u on o.ucenik=u.user_id left join
		// odeljenje od on od.id=u.odeljenje left join predaje p on p.odeljenje=od.id
		// where p.nastavnik=3 and p.odeljenje=7;
		
		//// SELECT * FROM AddressEntity a LEFT JOiN UserEntity u ON a.id==u.address WHERE
		// u.name="name"

	//	String sql = "SELECT a FROM AddressEntity a LEFT JOIN FETCH a.users u WHERE u.name=:name";
		
		String sql= "SELECT o FROM OcenaEntity o LEFT JOIN FETCH o.ucenik u LEFT JOIN FETCH u.odeljenje od LEFT JOIN FETCH od.predaje p WHERE p.nastavnik=:nastavnik AND "
				+ "p.odeljenje=:odeljenje";
		Query query = em.createQuery(sql);
		query.setParameter("nastavnik", nastavnik);
		query.setParameter("odeljenje", odeljenje);
		List<OcenaEntity> retVal = query.getResultList();
		return retVal;
	}

}
