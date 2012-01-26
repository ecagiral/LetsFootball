package com.erman.football.server.data;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.erman.football.shared.Schedule;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class ScheduleDO {
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    public void setKey(Key key) {
        this.key = key;
    }
    
    public long getKey(){
    	return key.getId();
    }
    
    @Persistent
    private long pitchId;

    @Persistent
    private long time;

	public long getPitchId() {
		return pitchId;
	}

	public void setPitchId(long pitchId) {
		this.pitchId = pitchId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	public static ScheduleDO convert(Schedule schedule){
		ScheduleDO scheduleDO = new ScheduleDO();
		scheduleDO.setPitchId(schedule.getPitch());
		scheduleDO.setTime(schedule.getDate());
		return scheduleDO;
	}
	
	public void update(Schedule schedule){
		this.setPitchId(schedule.getPitch());
		this.setTime(schedule.getDate());
	}
	
	public Schedule convert(){
		Schedule schedule = new Schedule();
		schedule.setDate(this.getTime());
		schedule.setPitch(this.getPitchId());
		return schedule;
	}

}
