package com.erman.football.server.data;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.erman.football.shared.Pitch;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class Pitch_JDO_DB {
	
	public static void addPitch(Pitch pitch){
		PitchDO pitchDO = PitchDO.generate(pitch);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(pitchDO);
		}catch(Exception e){
			System.out.println("unable to add pitch");
		} finally {
			pm.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<PitchDO> getPitches(int start, int stop){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<PitchDO> pitchDOs = new ArrayList<PitchDO>();
		Query query = pm.newQuery(PitchDO.class);
		query.setOrdering("name ascending");
		query.setRange(start,stop);
		try{
			pitchDOs = (List<PitchDO>)query.execute();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally{
			query.closeAll();
		}
		return pitchDOs;
	}
	
	public static void deletePitch(Long id){
		Key key = KeyFactory.createKey(PitchDO.class.getSimpleName(), id);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			PitchDO pitchDO = pm.getObjectById(PitchDO.class, key);
			pm.deletePersistent(pitchDO);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally {
			pm.close();
		}
	}
	
	public static void updatePitch(Pitch pitch){
		Key key = KeyFactory.createKey(PitchDO.class.getSimpleName(), pitch.getKey());
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			PitchDO pitchDO = pm.getObjectById(PitchDO.class, key);
			if (pitchDO!=null) {
				pitchDO.update(pitch);
			}else{
				//Could not be found
				throw new Exception("Pitch to be updated could not be found "+pitch.getName());
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally {
			pm.close();
		}
	}

}
