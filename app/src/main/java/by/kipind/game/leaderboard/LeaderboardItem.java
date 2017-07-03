package by.kipind.game.leaderboard;

import java.sql.Date;

public class LeaderboardItem {
private  int uid;
private  int leadPlase;
private  String country;
private  String nick;
private  float progVal;
private  String progUpDate ;

public int getUid() {
	return uid;
}
public void setUid(int uid) {
	this.uid = uid;
}
public int getLeadPlase() {
	return leadPlase;
}
public void setLeadPlase(int leadPlase) {
	this.leadPlase = leadPlase;
}
public String getCountry() {
	return country;
}
public void setCountry(String country) {
	this.country = country;
}
public String getNick() {
	return nick;
}
public void setNick(String nick) {
	this.nick = nick;
}
public float getProgVal() {
	return progVal;
}
public void setProgVal(float progVal) {
	this.progVal = progVal;
}
public String getProgUpDate() {
	return progUpDate;
}
public void setProgUpDate(String progUpDate) {
	this.progUpDate = progUpDate;
}

}
