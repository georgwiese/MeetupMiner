package de.hpi.smm.meetup_miner.formality.features;

import java.util.Arrays;

public class AbbreviationWords extends Feature{
	
	private String[] targetWords = {"e.g.","i.e.","etc.","ok","jan.","feb.","mar.","apr.","may.","jun.","jul.","aug.","sep.","oct.","nov.","dec.","sat.","sun.","mon.","tue.","wed.","thu.","fri.","plane","lab.","asap","usa","tons","undergrad.","grad.","hr","prof.","ai","ur","&","abt","mt","mt.","shd","shd.","wch","wd","wd.","wh","wh.","yr","yr.","yrs","yrs.","2d","2n","3d","acct","acct.","advt","aftern","aftern.","aftn","aftn.","am","a m","anti-cathc.","arc.","ass.","b.m.","bkfst","bkfst.","brkfst","c.b.e.","cent","chap","chap.","chaps","depart.","edn","eng.","h.oflds","hist.","hrs.","in.","ld.","ldy","ldy.","lovg","ly","ly.","max.","mem.","min.","min:","phys.","prob.","recvd","secty.","temp.","trans.","univ.","vol.","xmas","xtian","yestdy","acad.","adm.","bib."};
	
	public AbbreviationWords(){
		this.targetWordList = Arrays.asList(this.targetWords);
	}
}
