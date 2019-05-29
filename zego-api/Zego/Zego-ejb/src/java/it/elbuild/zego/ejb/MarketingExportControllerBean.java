/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb;

import el.persistence.db.DBTuple;
import it.elbuild.zego.entities.Driverdata;
import it.elbuild.zego.entities.Exportlink;
import it.elbuild.zego.entities.Feedback;
import it.elbuild.zego.entities.Nextipcall;
import it.elbuild.zego.entities.Promo;
import it.elbuild.zego.entities.Riderequest;
import it.elbuild.zego.entities.RiderequestDrivers;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.entities.Userpromo;
import it.elbuild.zego.iface.DAOProvider;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.iface.MarketingExportController;
import it.elbuild.zego.util.PromoReportEntry;
import it.elbuild.zego.util.RESTDateUtil;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Lu
 */
@Stateless
public class MarketingExportControllerBean implements MarketingExportController {

    @EJB
    DAOProvider provider;

    EntityController<Integer, User> userCtrl;
    EntityController<Integer, Driverdata> driverDataCtrl;
    EntityController<Integer, Riderequest> requestCtrl;
    EntityController<Integer, RiderequestDrivers> linkCtrl;
    EntityController<Integer, Exportlink> explinkCtrl;
    EntityController<Integer, Promo> promoCtrl;
    EntityController<Integer, Userpromo> userpromoCtrl;
    EntityController<Integer, Feedback> feedbackCtrl;
    EntityController<Integer, Nextipcall> nextipCallCtrl;

    private SimpleDateFormat DMY;
    private SimpleDateFormat MY;
    private SimpleDateFormat TS;
    private SimpleDateFormat HMS;
    private SimpleDateFormat HH;
    private NumberFormat NF;
    private int offsetMillis;

    @PostConstruct
    private void init() {
        userpromoCtrl = provider.provide(Userpromo.class);
        promoCtrl = provider.provide(Promo.class);
        userCtrl = provider.provide(User.class);
        requestCtrl = provider.provide(Riderequest.class);
        linkCtrl = provider.provide(RiderequestDrivers.class);
        explinkCtrl = provider.provide(Exportlink.class);
        driverDataCtrl = provider.provide(Driverdata.class);
        feedbackCtrl = provider.provide(Feedback.class);
        nextipCallCtrl = provider.provide(Nextipcall.class);

        DMY = new SimpleDateFormat("dd/MM/yyyy");
        MY = new SimpleDateFormat("MM/yyyy");
        TS = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        HMS = new SimpleDateFormat("HH:mm:ss");
        HH = new SimpleDateFormat("HH");
        NF = new DecimalFormat("0.00");
        offsetMillis = TimeZone.getTimeZone("Europe/Rome").getOffset(Calendar.getInstance().getTimeInMillis());

    }

    @Override
    public StringBuilder user(StringBuilder sb, String start, String stop) {
        List<User> uu = userCtrl.findListCustom("findByInsdateBetween", Arrays.asList(new DBTuple("start", start), new DBTuple("stop", stop)));
        sb.append("ID;Nome;Cognome;Data registrazione;Mese/anno registrazione;Email;Prefisso cellulare;"
                + "Cellulare;Versione app;Sistema operativo;Versione sistema operativo;Sesso;Fotografia;"
                + "Metodo pagamento ok;Pag solo via App;Ha richiesto di diventare driver;Driver abilitato;Codice MGM;"
                + "Data ultimo accesso;Citta’ ultima location;Data ultima richiesta effettuata;Data ultimo passaggio terminato come driver;"
                + "Numero passaggi terminati come driver mese precedente all’estrazione;Data ultimo passaggio terminato come rider;"
                + "Numero passaggi terminati come rider mese precedente all’estrazione;Numero richieste mese precedente all’estrazione;"
                + "Rating ricevuto come driver;Rating ricevuto come rider;Totale feedback ricevuti come driver;"
                + "Totale feedback ricevuti come rider;Totale feedback ricevuti 1 e 2 come driver;Totale feedback ricevuti 1 e 2 come rider\n");

        Calendar cx = Calendar.getInstance(TimeZone.getTimeZone("CET"));
        cx.set(Calendar.HOUR, 0);
        cx.set(Calendar.MINUTE, 0);
        cx.set(Calendar.SECOND, 0);
        cx.set(Calendar.DAY_OF_MONTH, 1);

        String repstop = String.valueOf(cx.getTime().getTime() / 1000);
        cx.add(Calendar.MONTH, -1);
        String repstart = String.valueOf(cx.getTime().getTime() / 1000);;

        for (User u : uu) {
            Driverdata dd = driverDataCtrl.findFirst("findByUid", Arrays.asList(new DBTuple("uid", u.getId())), false);
            sb.append(u.getId()).append(";");
            sb.append(u.getFname()).append(";");
            sb.append(u.getLname()).append(";");
            sb.append(DMY.format(RESTDateUtil.getInstance().formatDateSeconds(tz(u.getInsdate())))).append(";");
            sb.append(MY.format(RESTDateUtil.getInstance().formatDateSeconds(tz(u.getInsdate())))).append(";");
            sb.append(u.getEmail()).append(";");
            sb.append(u.getPrefix()).append(";");
            sb.append(u.getMobile()).append(";");
            sb.append(u.getVapp()).append(";");
            sb.append(u.getOs()).append(";");
            sb.append(u.getVos()).append(";");
            sb.append(n(u.getGender())).append(";");
            sb.append(u.getImg() == null ? "0" : "1").append(";");
            sb.append(u.getPayok() == null ? "0" : u.getPayok()).append(";");
            sb.append(u.getCardonly()== null || u.getCardonly().equals(0)? "0" : "1").append(";");
            sb.append(dd == null ? "0" : "1").append(";");
            sb.append(u.getCandrive()).append(";");
            sb.append(u.getRefcode()).append(";");
            sb.append(DMY.format(RESTDateUtil.getInstance().formatDateSeconds(tz(u.getLastseen() == null ? u.getInsdate() : u.getLastseen())))).append(";");
            sb.append(u.getArea() == null ? (dd == null ? "Non Disponibile" : dd.getArea()) : u.getArea()).append(";");

            List<Riderequest> pid = requestCtrl.findListCustom("findByPid", Arrays.asList(new DBTuple("pid", u.getId())), 0, 1);
            if (pid.isEmpty()) {
                sb.append("").append(";");
            } else {
                sb.append(DMY.format(RESTDateUtil.getInstance().formatDateSeconds(tz(pid.get(0).getReqdate())))).append(";");
            }

            List<Riderequest> did = requestCtrl.findListCustom("findByDid", Arrays.asList(new DBTuple("did", u.getId())), 0, 1);
            if (did.isEmpty()) {
                sb.append("").append(";");
            } else {
                sb.append(DMY.format(RESTDateUtil.getInstance().formatDateSeconds(tz(did.get(0).getReqdate())))).append(";");
            }

            Long didct = requestCtrl.countListCustom("countByDidBetween", Arrays.asList(new DBTuple("did", u.getId()), new DBTuple("start", repstart), new DBTuple("stop", repstop)));
            if (didct == null) {
                sb.append("0").append(";");
            } else {
                sb.append(didct).append(";");
            }

            List<Riderequest> didT = requestCtrl.findListCustom("findByPidTerminated", Arrays.asList(new DBTuple("pid", u.getId())), 0, 1);
            if (didT.isEmpty()) {
                sb.append("").append(";");
            } else {
                sb.append(DMY.format(RESTDateUtil.getInstance().formatDateSeconds(tz(didT.get(0).getReqdate())))).append(";");
            }

            Long didctT = requestCtrl.countListCustom("countByPidBetweenTerminated", Arrays.asList(new DBTuple("pid", u.getId()), new DBTuple("start", repstart), new DBTuple("stop", repstop)));
            if (didctT == null) {
                sb.append("0").append(";");
            } else {
                sb.append(didctT).append(";");
            }

            Long pidct = requestCtrl.countListCustom("countByPidBetween", Arrays.asList(new DBTuple("pid", u.getId()), new DBTuple("start", repstart), new DBTuple("stop", repstop)));
            if (pidct == null) {
                sb.append("0").append(";");
            } else {
                sb.append(pidct).append(";");
            }

            sb.append(u.getDavg()).append(";");
            sb.append(u.getPavg()).append(";");

            Long fed = feedbackCtrl.countListCustom("countByUidAsDriver", Arrays.asList(new DBTuple("uid", u.getId())));
            if (fed == null) {
                sb.append("0").append(";");
            } else {
                sb.append(fed).append(";");
            }

            Long fer = feedbackCtrl.countListCustom("countByUidAsRider", Arrays.asList(new DBTuple("uid", u.getId())));
            if (fer == null) {
                sb.append("0").append(";");
            } else {
                sb.append(fer).append(";");
            }

            Long fedi = feedbackCtrl.countListCustom("countLowByUidAsDriver", Arrays.asList(new DBTuple("uid", u.getId())));
            if (fedi == null) {
                sb.append("0").append(";");
            } else {
                sb.append(fedi).append(";");
            }

            Long feri = feedbackCtrl.countListCustom("countLowByUidAsRider", Arrays.asList(new DBTuple("uid", u.getId())));
            if (feri == null) {
                sb.append("0").append(";");
            } else {
                sb.append(feri).append("");
            }

            sb.append("\n");
        }

        return sb;
    }

    @Override
    public StringBuilder ride(StringBuilder sb, String start, String stop) {
        List<Riderequest> rr = requestCtrl.findListCustom("findByReqdateBetween", Arrays.asList(new DBTuple("start", start), new DBTuple("stop", stop)));
        sb.append("ID;Short ID;Data richiesta;Ora richiesta ;Fascia oraria richiesta;ID Passeggero;Città richiesta;"
                + "Indirizzo partenza richiesto;Indirizzo arrivo richiesto;Importo suggerito;Importo selezionato;Rimborso driver selezionato;"
                + "Fee zego selezionata;Codice promo;Numero Passeggeri;Km percorso da PU a DO;Status ride;Driver ID;Distanza driver da punto di partenza;"
                + "Tempo di arrivo del driver stimato;Tempo di accettazione;Indirizzo partenza reale;Indirizzo arrivo reale;Rating driver;Rating passeggero;"
                + "Motivo cancellazione;Motivo feedback negativo driver;Motivo feeback negativo passeggero;"
                + "Telefonata Driver/Passeggero;Importo da addebitare al Passeggero;Metodo Pagamento;Tipo Corso\n");

        for (Riderequest r : rr) {
            sb.append(r.getId()).append(";");
            sb.append(r.getShortid()).append(";");
            sb.append(DMY.format(RESTDateUtil.getInstance().formatDateSeconds(tz(r.getReqdate())))).append(";");
            sb.append(HMS.format(RESTDateUtil.getInstance().formatDateSeconds(tz(r.getReqdate())))).append(";");
            sb.append(HH.format(RESTDateUtil.getInstance().formatDateSeconds(tz(r.getReqdate())))).append(";");
            sb.append(r.getPid()).append(";");
            sb.append(r.getZonename()).append(";");
            sb.append(r.getPuaddr()).append(";");
            sb.append(r.getDoaddr()).append(";");
            sb.append(p(r.getExtprice())).append(";");
            sb.append(p(r.getPassprice())).append(";");
            sb.append(p(r.getDriverfee())).append(";");
            sb.append(p(r.getZegofee())).append(";");
            sb.append(r.getPromoid() == null || r.getPromoid().equals(0) ? "NO" : "SI").append(";");
            sb.append(r.getNumpass()).append(";");
            sb.append(k(r.getExtmeters())).append(";");
            sb.append(r.getStatus()).append(";");
            sb.append(r.getDid() == null || r.getDid().equals(0) ? "" : r.getDid()).append(";");
            RiderequestDrivers lk = r.getDid() == null ? null : linkCtrl.findFirst("findByRidAndDid", Arrays.asList(new DBTuple("rid", r.getId()), new DBTuple("did", r.getDid())), false);
            sb.append(lk == null ? "" : k(lk.getDmeters())).append(";");
            sb.append(lk == null ? "" : mmss(lk.getDsec())).append(";");
            sb.append(lk == null ? "" : mmss(Integer.parseInt(r.getAssigndate() == null ? r.getReqdate() : r.getAssigndate()) - Integer.parseInt(r.getReqdate()))).append(";");
            sb.append(n(r.getRealpuaddr())).append(";");
            sb.append(n(r.getRealdoaddr())).append(";");
            sb.append(r.getDrivrating() == null || r.getDrivrating().equals(0) ? "" : r.getDrivrating()).append(";");
            sb.append(r.getPassrating() == null || r.getPassrating().equals(0) ? "" : r.getPassrating()).append(";");
            sb.append(r.getCancelreason() == null ? "" : r.getCancelreason()).append(";");

            Feedback fdr = null;
            Feedback prd = null;
            if (r.getStatus().equals(10) || r.getStatus().equals(11) || r.getStatus().equals(12)) {
                List<Feedback> ff = feedbackCtrl.findListCustom("findByRid", Arrays.asList(new DBTuple("rid", r.getId())));
                for (Feedback f : ff) {
                    if (f.getFromdriver().equals(0)) {
                        prd = f;
                    } else {
                        fdr = f;
                    }
                }
            }
            sb.append(fdr == null || fdr.getReason() == null ? "" : fdr.getReason()).append(";");
            sb.append(prd == null || prd.getReason() == null ? "" : prd.getReason()).append(";");
            Nextipcall nc = null;
            if (r.getStatus().equals(10) || r.getStatus().equals(11) || r.getStatus().equals(12)) {
                nc = nextipCallCtrl.findFirst("findByRid", Arrays.asList(new DBTuple("rid", r.getId())), false);
            }
            sb.append(nc == null ? "" : TS.format(RESTDateUtil.getInstance().formatDateSeconds(tz(nc.getCalldate())))).append(";");
            sb.append(p(r.getStripedriverfee() + r.getStripezegofee())).append(";");
            sb.append(r.getMethod() == null || !r.getMethod().equals("cash") ? "Carta di credito" : "Cash").append(";");
            sb.append(r.getReqlevel() == null || r.getReqlevel().equals(Riderequest.REQUEST_LEVEL_STANDARD) ? "Standard" : 
                   r.getReqlevel().equals(Riderequest.REQUEST_LEVEL_PINK) ? "Corsa Rosa" : "Corsa Control" ).append("");
            sb.append("\n");

        }

        return sb;
    }

    @Override
    public StringBuilder link(StringBuilder sb, String start, String stop) {
        List<Exportlink> ll = explinkCtrl.findListCustom("findByReqdateBetween", Arrays.asList(new DBTuple("start", start), new DBTuple("stop", stop)));
        sb.append("Ride code;Ride short ID;Data;Ora;Fascia oraria;Città;Passeggero ID;Km percorso da PU a DO ;"
                + "rimborso driver selezionato;totale driver ingaggiati;driver ingaggiato;"
                + "Distanza driver ingaggiato da punto di partenza;Scelta driver ingaggiato;Status ride\n");
        HashMap<Integer, Integer> ing = new HashMap<>();

        for (Exportlink l : ll) {
            if (!ing.containsKey(l.getRid())) {
                ing.put(l.getRid(), 0);
            }
            ing.put(l.getRid(), ing.get(l.getRid()) + 1);
        }

        for (Exportlink l : ll) {

            sb.append(l.getRid()).append(";");
            sb.append(l.getShortid()).append(";");
            sb.append(DMY.format(RESTDateUtil.getInstance().formatDateSeconds(tz(l.getReqdate())))).append(";");
            sb.append(HMS.format(RESTDateUtil.getInstance().formatDateSeconds(tz(l.getReqdate())))).append(";");
            sb.append(HH.format(RESTDateUtil.getInstance().formatDateSeconds(tz(l.getReqdate())))).append(";");
            sb.append(l.getZonename()).append(";");
            sb.append(l.getPid()).append(";");
            sb.append(k(l.getExtmeters())).append(";");
            sb.append(l.getDriverfee()).append(";");
            sb.append(ing.get(l.getRid())).append(";");
            sb.append(l.getDid()).append(";");
            sb.append(k(l.getDmeters())).append(";");
            sb.append(ls(l.getLinkstatus())).append(";");
            sb.append(l.getRidestatus()).append("\n");
        }
        return sb;
    }

    @Override
    public StringBuilder promo(StringBuilder sb, String start, String stop) {
        List<Userpromo> uu = userpromoCtrl.findListCustom("findByPromoRedeemdateBetween", Arrays.asList(new DBTuple("start", start), new DBTuple("stop", stop)));

        sb.append("CODICE PROMO;TIPO PROMO;ID UTENTE ;Presentante;DATA ATTIVAZIONE PROMO;DATA SCADENZA PROMO;DATA UTILIZZO PROMO;Tipo Burn\n");

        for (Userpromo u : uu) {
            sb.append(u.getPromo().getCode()).append(";");
            if (u.getPromo().getCode().startsWith("AWA-")) {
                sb.append("AWA").append(";");
            } else if (u.getPromo().getCode().startsWith("MGM-")) {
                sb.append("MGM").append(";");
            } else {
                sb.append(u.getPromo().getType()).append(";");
            }
            sb.append(u.getUid()).append(";");
            if (u.getPromo().getCode().startsWith("MGM-")) {
                User us = userCtrl.findById(u.getUid());
                if (us != null && us.getRefuid() != null) {
                    User rf = userCtrl.findById(us.getRefuid());
                    if (rf != null) {
                        sb.append(rf.getId()).append(";");
                    } else {
                        sb.append("").append(";");
                    }
                } else {
                    sb.append("").append(";");
                }
            } else {
                sb.append("").append(";");
            }

            sb.append(u.getRedeemdate() == null ? "" : TS.format(RESTDateUtil.getInstance().formatDateSeconds(tz(u.getRedeemdate())))).append(";");
            sb.append(u.getExpirydate() == null ? "" : TS.format(RESTDateUtil.getInstance().formatDateSeconds(tz(u.getExpirydate())))).append(";");
            sb.append(u.getUsagedate() == null ? "" : TS.format(RESTDateUtil.getInstance().formatDateSeconds(tz(u.getUsagedate())))).append(";");
            sb.append(b(u.getBurnt())).append("\n");

        }

        sb.append("\n");

        return sb;
    }

    @Override
    public StringBuilder fullpromo(StringBuilder sb, String start, String stop) {
        Long or = System.currentTimeMillis();
        List<Userpromo> activatedBetween = userpromoCtrl.findListCustom("findByPromoRedeemdateBetween", Arrays.asList(new DBTuple("start", start), new DBTuple("stop", stop)));
        System.out.println("CACHE QUERY 1 " + (System.currentTimeMillis() - or));
        List<Userpromo> usedBetween = userpromoCtrl.findListCustom("findByUsagedateBetween", Arrays.asList(new DBTuple("start", start), new DBTuple("stop", stop)));
        System.out.println("CACHE QUERY 2 " + (System.currentTimeMillis() - or));
        List<Riderequest> reqMap = requestCtrl.findListCustom("findByPromoAndReqdateBetween", Arrays.asList(new DBTuple("start", start), new DBTuple("stop", stop)));
        System.out.println("CACHE QUERY 3 " + (System.currentTimeMillis() - or));

        HashMap<String, Integer> usageMapCB = new HashMap<>();
        HashMap<Integer, ArrayList<Riderequest>> reqCache = new HashMap<>();
        HashMap<String, Integer> usageMapSignup = new HashMap<>();
        HashMap<String, Integer> redeemMapCB = new HashMap<>();
        HashMap<String, Integer> redeemMapSignup = new HashMap<>();
        HashMap<String, List<Integer>> valMapSignup = new HashMap<>();
        HashMap<String, List<Integer>> valMapCB = new HashMap<>();

        for (Riderequest rq : reqMap) {
            if (rq.getPromoid() != null && rq.getPromoid() > 0) {
                if (!reqCache.containsKey(rq.getPromoid())) {
                    reqCache.put(rq.getPromoid(), new ArrayList<Riderequest>());
                }
                reqCache.get(rq.getPromoid()).add(rq);
            }
        }

        System.out.println("CACHE BUILT 1 " + (System.currentTimeMillis() - or));

        for (Userpromo up : activatedBetween) {
            User u = userCtrl.findById(up.getUid());
            if (up.getPromo() != null && u != null) {

                boolean signup = Math.abs(Long.parseLong(u.getInsdate() == null ? "0" : u.getInsdate()) - Long.parseLong(up.getRedeemdate())) < 10 * 60;
                if (signup) {
                    if (!redeemMapSignup.containsKey(up.getPromo().getCode())) {
                        redeemMapSignup.put(up.getPromo().getCode(), 0);
                    }
                    redeemMapSignup.put(up.getPromo().getCode(), redeemMapSignup.get(up.getPromo().getCode()) + 1);
                } else {
                    if (!redeemMapCB.containsKey(up.getPromo().getCode())) {
                        redeemMapCB.put(up.getPromo().getCode(), 0);
                    }
                    redeemMapCB.put(up.getPromo().getCode(), redeemMapCB.get(up.getPromo().getCode()) + 1);
                }
            }
        }

        System.out.println("CACHE BUILT 2 " + (System.currentTimeMillis() - or));

        for (Userpromo up : usedBetween) {
            User u = userCtrl.findById(up.getUid());
            if (up.getPromo() != null && u != null) {
                boolean signup = Math.abs(Long.parseLong(u.getInsdate() == null ? "0" : u.getInsdate()) - Long.parseLong(up.getRedeemdate())) < 10 * 60;
                if (signup) {
                    if (!usageMapSignup.containsKey(up.getPromo().getCode())) {
                        usageMapSignup.put(up.getPromo().getCode(), 0);
                        valMapSignup.put(up.getPromo().getCode(), new ArrayList<Integer>());
                    }
                    usageMapSignup.put(up.getPromo().getCode(), usageMapSignup.get(up.getPromo().getCode()) + 1);
                    valMapSignup.get(up.getPromo().getCode()).add(up.getId());
                } else {
                    if (!usageMapCB.containsKey(up.getPromo().getCode())) {
                        usageMapCB.put(up.getPromo().getCode(), 0);
                        valMapCB.put(up.getPromo().getCode(), new ArrayList<Integer>());
                    }
                    usageMapCB.put(up.getPromo().getCode(), usageMapCB.get(up.getPromo().getCode()) + 1);
                    valMapCB.get(up.getPromo().getCode()).add(up.getId());
                }
            }
        }

        System.out.println("CACHE BUILT 3 " + (System.currentTimeMillis() - or));

        Integer actTot = 0;
        Integer actSignup = 0;
        Integer actCB = 0;
        List<PromoReportEntry> actSignupList = new ArrayList<>();
        List<PromoReportEntry> actCBList = new ArrayList<>();
        for (String k : redeemMapSignup.keySet()) {
            Integer v = redeemMapSignup.get(k);
            actTot += v;
            actSignup += v;
            actSignupList.add(new PromoReportEntry(k, v));
        }

        for (String k : redeemMapCB.keySet()) {
            Integer v = redeemMapCB.get(k);
            actTot += v;
            actCB += v;
            actCBList.add(new PromoReportEntry(k, v));
        }

        Integer useTot = 0;
        Integer useSignup = 0;
        Integer useCB = 0;
        List<PromoReportEntry> useSignupList = new ArrayList<>();
        List<PromoReportEntry> useCBList = new ArrayList<>();

        for (String k : usageMapSignup.keySet()) {
            Integer v = usageMapSignup.get(k);
            useTot += v;
            useSignup += v;
            useSignupList.add(new PromoReportEntry(k, v));
        }

        for (String k : usageMapCB.keySet()) {
            Integer v = usageMapCB.get(k);
            useTot += v;
            useCB += v;
            useCBList.add(new PromoReportEntry(k, v));
        }

        Integer valTot = 0;
        Integer valSignup = 0;
        Integer valCB = 0;
        List<PromoReportEntry> valSignupList = new ArrayList<>();
        List<PromoReportEntry> valCBList = new ArrayList<>();

        for (String k : valMapSignup.keySet()) {
            Integer v = 0;
            for (Integer i : valMapSignup.get(k)) {
                List<Riderequest> rr = reqCache.get(i);// requestCtrl.findListCustom("findByPromoid", Arrays.asList(new DBTuple("promoid", i)));
                if (rr != null) {
                    for (Riderequest r : rr) {
                        if (r.getStatus() > 9 && r.getStatus() < 13) {
                            v = v + r.getDiscount();
                        }
                    }
                }
            }

            valTot += v;
            valSignup += v;
            valSignupList.add(new PromoReportEntry(k, v));
        }

        for (String k : usageMapCB.keySet()) {
            Integer v = 0;
            for (Integer i : valMapCB.get(k)) {
                List<Riderequest> rr = reqCache.get(i);// requestCtrl.findListCustom("findByPromoid", Arrays.asList(new DBTuple("promoid", i)));
                if (rr != null) {
                    for (Riderequest r : rr) {
                        if (r.getStatus() > 9 && r.getStatus() < 13) {
                            v = v + r.getDiscount();
                        }
                    }
                }
            }
            valTot += v;
            valCB += v;
            valCBList.add(new PromoReportEntry(k, v));
        }

        // compose the file, line byy line, 3 columns, some of them are empty
        sb.append("CAMPO;;VALORE\n");
        sb.append("TOTALE ATTIVATE;;").append(actTot).append("\n");
        sb.append("Fase registrazione;;").append(actSignup).append("\n");
        sb = appendList(actSignupList, sb);
        sb.append("Customer Base;;").append(actCB).append("\n");
        sb = appendList(actCBList, sb);
        sb.append("\n");
        sb.append("PROMOZIONI UTILIZZATE;;").append(useTot).append("\n");
        sb.append("Fase registrazione;;").append(useSignup).append("\n");
        sb = appendList(useSignupList, sb);
        sb.append("Customer Base;;").append(useCB).append("\n");
        sb = appendList(useCBList, sb);
        sb.append("\n");
        sb.append("VALORE PROMO UTILIZZATE;;").append(p(valTot)).append("\n");
        sb.append("Fase registrazione;;").append(p(valSignup)).append("\n");
        sb = appendPList(valSignupList, sb);
        sb.append("Customer Base;;").append(p(valCB)).append("\n");
        sb = appendPList(valCBList, sb);
        sb.append("\n");
        return sb;
    }

    private StringBuilder appendList(List<PromoReportEntry> pl, StringBuilder sb) {
        Collections.sort(pl);
        for (PromoReportEntry pe : pl) {
            sb.append(";").append(pe.getCode()).append(";").append(pe.getValue()).append("\n");
        }
        return sb;
    }

    private StringBuilder appendPList(List<PromoReportEntry> pl, StringBuilder sb) {
        Collections.sort(pl);
        for (PromoReportEntry pe : pl) {
            sb.append(";").append(pe.getCode()).append(";").append(p(pe.getValue())).append("\n");
        }
        return sb;
    }

    @Override
    public StringBuilder signup(StringBuilder sb, String start, String stop) {
        List<String> cities = Arrays.asList("Torino", "Milano", "Genova", "Bologna", "N.A.");
        List<DBTuple> ss = Arrays.asList(new DBTuple("start", start), new DBTuple("stop", stop));
        List<DBTuple> epoch = Arrays.asList(new DBTuple("start", "0"), new DBTuple("stop", RESTDateUtil.getInstance().secondsNow()));
        /*
         REGISTRAZIONI
         ow In progress
         ow pagamento ok
         foto ok su chi ha pagamento ok
         */
        sb.append("REGISTRAZION;").append(userCtrl.countListCustom("countBetween", ss)).append("\n");
        sb.append("ow In progress;").append(userCtrl.countListCustom("countInprogressBetween", ss)).append("\n");
        sb.append("ow pagamento ok;").append(userCtrl.countListCustom("countPayOkBetween", ss)).append("\n");
        sb.append("foto ok su chi ha pagamento ok;").append(userCtrl.countListCustom("countAllOkBetween", ss)).append("\n");
        /*
         totale registrazione città1
         totale registrazione città2
         totale registrazione città3
         totale registrazione città unknown
         */
        for (String c : cities) {
            sb.append("totale registrazione ").append(c).append(";").append(userCtrl.countListCustom("countByCityBetween", Arrays.asList(new DBTuple("area", c), new DBTuple("start", start), new DBTuple("stop", stop)))).append("\n");
        }
        sb.append("\n\n");
        /*
         TOTALE REGISTRATI
         TOTALE città1 (last position = citta')
         TOTALE città2
         TOTALE città3
         */
        Long overall = userCtrl.countListCustom("countBetween", epoch);
        Long current = 0l;
        sb.append("TOTALE REGISTRATI;").append(";").append(overall).append("\n");
        for (String c : cities) {
            current = userCtrl.countListCustom("countByCityBetween", Arrays.asList(new DBTuple("area", c), new DBTuple("start", "0"), new DBTuple("stop", RESTDateUtil.getInstance().secondsNow())));
            overall = overall - current;
            if (c.equalsIgnoreCase("N.A.")) {
                sb.append("TOTALE ").append(c).append(";").append(overall).append("\n");
            } else {
                sb.append("TOTALE ").append(c).append(";").append(current).append("\n");
            }
        }
        sb.append("\n\n");

        /*
         TOTALE pagamento Ok su EOP
         TOTALE foto ok su TOTALE pagamento OK
         */
        sb.append("TOTALE pagamento Ok su EOP;").append(userCtrl.countListCustom("countPayOkBetween", epoch)).append("\n");
        sb.append("TOTALE foto ok su TOTALE pagamento OK;").append(userCtrl.countListCustom("countAllOkBetween", epoch)).append("\n");

        /*
         RICHIESTE PER DIVENTARE DRIVER (Driver che hanno selezionato la citta')
         città1
         città2
         città3
         */
        sb.append("\n\n");
        sb.append("RICHIESTE PER DIVENTARE DRIVER (Driver che hanno selezionato la citta')\n");
        for (String c : cities) {
            if (c.equals("N.A.")) {
                sb.append(c).append(";").append(driverDataCtrl.countListCustom("countByStatusAndAreaNA", Arrays.asList(new DBTuple("status", 1), new DBTuple("start", start), new DBTuple("stop", stop), new DBTuple("area", cities)))).append("\n");
            } else {
                sb.append(c).append(";").append(driverDataCtrl.countListCustom("countByStatusAndArea", Arrays.asList(new DBTuple("status", 1), new DBTuple("area", c), new DBTuple("start", start), new DBTuple("stop", stop)))).append("\n");
            }
        }
        /*
         DRIVER RICHIESTA EFFETTUATA e Inviata
         città1
         città2
         città3
         */
        sb.append("\n\n");
        sb.append("DRIVER RICHIESTA EFFETTUATA e Inviata\n");
        for (String c : cities) {
            sb.append(c).append(";").append(driverDataCtrl.countListCustom("countByStatusInAndArea", Arrays.asList(new DBTuple("status", Arrays.asList(1, 5)), new DBTuple("area", c), new DBTuple("start", start), new DBTuple("stop", stop)))).append("\n");
        }

        sb.append("\n\n");
        sb.append("VALIDAZIONI DRIVER NEL PERIODO\n");
        for (String c : cities) {
            sb.append(c).append(";").append(userCtrl.countListCustom("countCanDriveByCityBetween",
                    Arrays.asList(new DBTuple("area", c), new DBTuple("start", start), new DBTuple("stop", stop)))).append("\n");
        }


        /*
         EOP DRIVER VALIDATI
         città1
         città2
         città3
         */
        sb.append("\n\n");
        sb.append("EOP DRIVER VALIDATI\n");

        Long other = userCtrl.countListCustom("countCanDriveBetween", Arrays.asList(new DBTuple("start", "0"), new DBTuple("stop", RESTDateUtil.getInstance().secondsNow())));
        Long ct = 0l;
        for (String c : cities) {
            ct = userCtrl.countListCustom("countCanDriveByCityBetween", Arrays.asList(new DBTuple("area", c), new DBTuple("start", "0"), new DBTuple("stop", RESTDateUtil.getInstance().secondsNow())));
            other = other - ct;
            if (c.equalsIgnoreCase("N.A.")) {
                sb.append(c).append(";").append(other).append("\n");
            } else {
                sb.append(c).append(";").append(ct).append("\n");
            }
        }
        return sb;
    }

    @Override
    public StringBuilder fullride(StringBuilder sb, String start, String stop, Integer city) {
        List<Riderequest> rx;
        if(city == 0) {
                    rx = requestCtrl.findListCustom("findByReqdateBetween", Arrays.asList(new DBTuple("start", start), new DBTuple("stop", stop))); 
        } else {
         rx = requestCtrl.findListCustom("findByReqdateBetweenZid", Arrays.asList(new DBTuple("start", start), new DBTuple("zid", city), new DBTuple("stop", stop)));
        }
        Integer totreq = rx.size();
        ArrayList<Riderequest> rr = new ArrayList<>();
        int j = 0;
        Collections.reverse(rx);
        for(Riderequest r : rx) {
            if(j == 0) {
                rr.add(r);
            } else if(r.getStatus().equals(Riderequest.REQUEST_STATUS_PASSENGER_ABORTED) || r.getStatus().equals(Riderequest.REQUEST_STATUS_DRIVER_CANCELED) || 
                    r.getStatus().equals(Riderequest.REQUEST_STATUS_ABORTED_UNPAID) || r.getStatus().equals(Riderequest.REQUEST_STATUS_DRIVER_ABORTED) || 
                    r.getStatus().equals(Riderequest.REQUEST_STATUS_PAID) || r.getStatus().equals(Riderequest.REQUEST_STATUS_ENDED) || 
                    r.getStatus().equals(Riderequest.REQUEST_PAYMENT_FAILED) || r.getStatus().equals(Riderequest.REQUEST_STATUS_PASSENGER_TERMINATED) || 
                    r.getStatus().equals(Riderequest.REQUEST_STATUS_REFUNDED)) {
                rr.add(r);
            } else {
                boolean add = true;
                for(int k = rr.size()-1; k>=0; k--) {
                    Integer delta = Integer.valueOf(rr.get(k).getReqdate()) - Integer.valueOf(r.getReqdate());
                    if(delta > 300) {
                        break;
                    }
                    
                    if(Objects.equals(rr.get(k).getPid(), r.getPid())) {
                        add = false;
                        break;
                    }
                }
                
                if(add) {
                    rr.add(r);
                }
            }                       
            
            j++;
        }
        Collections.reverse(rr);
        Integer realreq = 0;
        Integer loweredPass = 0;
        Integer loweredDrv = 0;
        Integer loweredEndPass = 0;
        Integer loweredEndDrv = 0;
        Set<Integer> pas = new HashSet<>();
        Set<Integer> acceptedDri = new HashSet<>();
        Set<Integer> terminatedPas = new HashSet<>();
        Set<Integer> terminatedDri = new HashSet<>();
        Double avgKm = 0d;
        Double avgReimbSugg = 0d;
        Double avgReimbPass = 0d;
        Double avgAccept = 0d;
        Double avgAcceptDist = 0d;
        Double avgAcceptKm = 0d;
        Double avgReimbSel = 0d;

        Double avgStart = 0d;
        Double avgEndKm = 0d;
        Double avgEndReimb = 0d;
        Double avgEndFee = 0d;
        HashMap<Integer, Integer> states = new HashMap<>();
        for (Integer i = 0; i < 20; i++) {
            states.put(i, 0);
        }
        
        for (Riderequest r : rr) {
            realreq = rr.size();
            if (!states.containsKey(r.getStatus())) {
                states.put(r.getStatus(), 0);
            }
            states.put(r.getStatus(), states.get(r.getStatus()) + 1);

            avgKm += r.getExtmeters();
            avgReimbSugg += r.getExtprice();
            avgReimbPass += r.getPassprice();
            loweredPass = loweredPass + ((r.getPassprice() < r.getExtprice()) ? 1 : 0);
            loweredDrv = loweredDrv + ((r.getDriverprice() > 0 && (r.getDriverprice() < r.getExtprice()) && r.getDiscount() <= 0) ? 1 : 0);
            pas.add(r.getPid());

            // non accettate
            if (Arrays.asList(Riderequest.REQUEST_STATUS_IDLE, Riderequest.REQUEST_STATUS_SUBMITTED, Riderequest.REQUEST_STATUS_DRIVER_ANSWERED,
                    Riderequest.REQUEST_STATUS_NO_DRIVERS, Riderequest.REQUEST_STATUS_PASSENGER_CANCELED, Riderequest.REQUEST_MISSING_FUNDS,
                    Riderequest.REQUEST_CANCELLED_BY_SERVER).contains(r.getStatus())) {

            } else if (Arrays.asList(Riderequest.REQUEST_STATUS_PASSENGER_ABORTED, Riderequest.REQUEST_STATUS_DRIVER_CANCELED, 
                    Riderequest.REQUEST_STATUS_ABORTED_UNPAID, Riderequest.REQUEST_STATUS_DRIVER_ABORTED, 
                    Riderequest.REQUEST_STATUS_PAID, Riderequest.REQUEST_STATUS_ENDED, 
                    Riderequest.REQUEST_PAYMENT_FAILED, Riderequest.REQUEST_STATUS_PASSENGER_TERMINATED, 
                    Riderequest.REQUEST_STATUS_REFUNDED).contains(r.getStatus())) {
                acceptedDri.add(r.getDid());
                avgAcceptKm += r.getExtmeters();
                avgAccept += Long.parseLong(r.getAssigndate()) - Long.parseLong(r.getReqdate());
                avgReimbSel += Math.max(r.getPassprice(), r.getExtprice());
            } else {
                acceptedDri.add(r.getDid());
                avgStart += Long.parseLong(r.getStartdate() == null ? r.getAssigndate() : r.getStartdate()) - Long.parseLong(r.getReqdate());
                avgEndKm += r.getExtmeters();
                avgEndFee += r.getStripezegofee();
                avgEndReimb += r.getDriverfee();
                terminatedPas.add(r.getPid());
                terminatedDri.add(r.getDid());
                loweredEndPass = loweredEndPass + ((r.getPassprice() < r.getExtprice()) ? 1 : 0);
                loweredEndDrv = loweredEndDrv + ((r.getDriverprice() > 0 && (r.getDriverprice() < r.getExtprice()) && r.getDiscount() <= 0) ? 1 : 0);
            }
           
        }

        sb.append("RICHIESTE TOTALI;").append(totreq).append("\n");
        sb.append("RICHIESTE TOTALI VERE;").append(realreq).append("\n");
        sb.append("PASSEGGERI UNICI;").append(pas.size()).append("\n\n");

        sb.append("KM Medi Richiesta;").append(v(NF.format(avgKm / (rr.size() * 1000.d)))).append("\n");
        sb.append("MEDIA RIMBORSO SUGGERITO;").append(v(NF.format(avgReimbSugg / (100 * rr.size())))).append("\n");
        sb.append("MEDIA RIMBORSO SELEZIONATO PASS;").append(v(NF.format(avgReimbPass / (100 * rr.size())))).append("\n");
        sb.append("Numero ride con rimborso abbassato;").append(v(NF.format(loweredPass))).append("\n\n");

        /*CANCELLATE O EXPIRED
         no drivers
         cancellate prima dell'accettazione
         expired
         Altri stati (es idle o vari)
         */
        Integer exctr = states.get(Riderequest.REQUEST_STATUS_NO_DRIVERS) + states.get(Riderequest.REQUEST_STATUS_PASSENGER_CANCELED)
                + states.get(Riderequest.REQUEST_CANCELLED_BY_SERVER) + states.get(Riderequest.REQUEST_STATUS_IDLE)
                + states.get(Riderequest.REQUEST_STATUS_SUBMITTED);

        sb.append("CANCELLATE O EXPIRED;").append(exctr).append("\n");
        sb.append("no drivers;").append(states.get(Riderequest.REQUEST_STATUS_NO_DRIVERS)).append("\n");
        sb.append("cancellate prima dell'accettazione;").append(states.get(Riderequest.REQUEST_STATUS_PASSENGER_CANCELED)).append("\n");
        sb.append("expired;").append(states.get(Riderequest.REQUEST_CANCELLED_BY_SERVER)).append("\n");
        sb.append("Altri stati (es idle o vari);").append(states.get(Riderequest.REQUEST_STATUS_IDLE)
                + states.get(Riderequest.REQUEST_STATUS_SUBMITTED)).append("\n\n");

        Integer accptCtr = states.get(Riderequest.REQUEST_STATUS_DRIVER_ABORTED) + states.get(Riderequest.REQUEST_STATUS_DRIVER_ABORTED) + states.get(Riderequest.REQUEST_STATUS_DRIVER_CANCELED)
                + states.get(Riderequest.REQUEST_STATUS_PASSENGER_TERMINATED) + states.get(Riderequest.REQUEST_STATUS_ABORTED_UNPAID)
                + states.get(Riderequest.REQUEST_STATUS_ON_RIDE);

        Integer endCtr = states.get(Riderequest.REQUEST_STATUS_ENDED) + states.get(Riderequest.REQUEST_STATUS_PAID)
                + states.get(Riderequest.REQUEST_PAYMENT_FAILED) + states.get(Riderequest.REQUEST_STATUS_PASSENGER_TERMINATED);
        /*
         ACCETTATE
         Driver unici ( che hanno accettato almeno un passaggio)
         abortita dal driver
         abortita dal passeggero
         terminata ma rimborsata al passeggero (REFUNDED)
         */
        sb.append("ACCETTATE;").append(accptCtr + endCtr).append("\n");
        sb.append("Driver unici ( che hanno accettato almeno un passaggio);").append(acceptedDri.size()).append("\n");
        sb.append("abortita dal driver;").append(states.get(Riderequest.REQUEST_STATUS_DRIVER_ABORTED)).append("\n");
        sb.append("abortita dal passeggero;").append(states.get(Riderequest.REQUEST_STATUS_PASSENGER_ABORTED)).append("\n");
        sb.append("terminata ma rimborsata al passeggero (REFUNDED);").append(states.get(Riderequest.REQUEST_STATUS_REFUNDED)).append("\n");
        sb.append("cancellata dal passeggero dopo accettazione ma unpaid;").append(states.get(Riderequest.REQUEST_STATUS_ABORTED_UNPAID)).append("\n\n");

        /*
         Tempo medio accettazione
         Distanza media del driver cha accettato
         Km medi richiesta
         MEDIA RIMBORSO SELEZIONATO
         */
        sb.append("Tempo medio accettazione;").append(mmss(((Double) (avgAccept / accptCtr)).intValue())).append("\n");
        sb.append("Km medi richiesta;").append(v(NF.format(avgAcceptKm / (accptCtr * 1000.d)))).append("\n");
        sb.append("MEDIA RIMBORSO SELEZIONATO;").append(v(NF.format(avgReimbSel / (100 * accptCtr)))).append("\n\n");

        /*
         TERMINATE
         Tempo medio di inizio viaggio da richiesta
         terminata (stato 9)
         terminate e pagate (stato 10)
         terminate dal passeggero (11)
         terminate ma pagamento fallito (stato 12)
         */
        sb.append("TERMINATED;").append(endCtr).append("\n");
        sb.append("Tempo medio di inizio viaggio da richiesta;").append(0).append("\n");
        sb.append("terminata;").append(states.get(Riderequest.REQUEST_STATUS_ENDED)).append("\n");
        sb.append("terminate e pagate;").append(states.get(Riderequest.REQUEST_STATUS_PAID)).append("\n");
        sb.append("terminate dal passeggero;").append(states.get(Riderequest.REQUEST_STATUS_PASSENGER_TERMINATED)).append("\n");
        sb.append("terminate ma pagamento fallito;").append(states.get(Riderequest.REQUEST_PAYMENT_FAILED)).append("\n\n");

        /*
         PASSEGGERI UNICI
         utilizzi per passeggero unico (totale terminate/pass unico)
         Driver unici (che hanno terminato almeno un passaggio)
         */
        sb.append("Passeggeri unici che hanno terminato almeno un passsaggio;").append(terminatedPas.size()).append("\n");
        sb.append("utilizzi per passeggero unico (totale terminate/pass unico);").append(v(NF.format(endCtr / (0.d + terminatedPas.size())))).append("\n");
        sb.append("Driver unici (che hanno terminato almeno un passaggio);").append(terminatedDri.size()).append("\n");

        /* 
         Km medi terminate
         MEDIA RIMBORSO SELEZIONATO
         MEDIA FEE ZEGO
         Numero ride con rimborso abbassato dal pass
         Numero ride con rimborso abbassato dal driver
         */
        sb.append("Km medi terminate;").append(v(NF.format(avgEndKm / (endCtr * 1000.d)))).append("\n");
        sb.append("MEDIA RIMBORSO SELEZIONATO;").append(v(NF.format(avgEndReimb / (100 * endCtr)))).append("\n");
        sb.append("MEDIA FEE ZEGO;").append(v(String.valueOf(avgEndFee / (100 * endCtr)))).append("\n");
        sb.append("Numero ride con rimborso abbassato dal pass;").append(loweredEndPass).append("\n");

        return sb;
    }

//    @Override
//    public StringBuilder fullridefixed(StringBuilder sb, String start, String stop, Integer city) {
//        List<Riderequest> rr = requestCtrl.findListCustom("findByReqdateBetweenZid", Arrays.asList(new DBTuple("start", start), new DBTuple("zid", city), new DBTuple("stop", stop)));
//        Integer realreq = 0;
//        Integer loweredPass = 0;
//        Integer loweredDrv = 0;
//        Integer loweredEndPass = 0;
//        Integer loweredEndDrv = 0;
//        Set<Integer> pas = new HashSet<>();
//        Set<Integer> acceptedDri = new HashSet<>();
//        Set<Integer> terminatedPas = new HashSet<>();
//        Set<Integer> terminatedDri = new HashSet<>();
//        Double avgKm = 0d;
//        Double avgReimbSugg = 0d;
//        Double avgReimbPass = 0d;
//        Double avgAccept = 0d;
//        Double avgAcceptDist = 0d;
//        Double avgAcceptKm = 0d;
//        Double avgReimbSel = 0d;
//
//        Double avgStart = 0d;
//        Double avgEndKm = 0d;
//        Double avgEndReimb = 0d;
//        Double avgEndFee = 0d;
//        HashMap<Integer, Integer> states = new HashMap<>();
//        for (Integer i = 0; i < 20; i++) {
//            states.put(i, 0);
//        }
//
//        Integer i = 0;
//        for (Riderequest r : rr) {
//            if (!states.containsKey(r.getStatus())) {
//                states.put(r.getStatus(), 0);
//            }
//            states.put(r.getStatus(), states.get(r.getStatus()) + 1);
//
//            avgKm += r.getExtmeters();
//            avgReimbSugg += r.getExtprice();
//            avgReimbPass += r.getPassprice();
//            loweredPass = loweredPass + ((r.getPassprice() < r.getExtprice()) ? 1 : 0);
//            loweredDrv = loweredDrv + ((r.getDriverprice() > 0 && (r.getDriverprice() < r.getExtprice()) && r.getDiscount() <= 0) ? 1 : 0);
//            realreq = realreq + ((i.equals(0)
//                    || !rr.get(i - 1).getPid().equals(r.getPid())
//                    || (rr.get(i - 1).getPid().equals(r.getPid()) && Math.abs(Long.parseLong(r.getReqdate()) - Long.parseLong(rr.get(i - 1).getReqdate())) > 300)) ? 1 : 0);
//            pas.add(r.getPid());
//
//            // non accettate
//            if (Arrays.asList(Riderequest.REQUEST_STATUS_IDLE, Riderequest.REQUEST_STATUS_SUBMITTED,
//                    Riderequest.REQUEST_STATUS_NO_DRIVERS, Riderequest.REQUEST_STATUS_PASSENGER_CANCELED,
//                    Riderequest.REQUEST_CANCELLED_BY_SERVER).contains(r.getStatus())) {
//
//            } else if (Arrays.asList(Riderequest.REQUEST_STATUS_DRIVER_ABORTED, Riderequest.REQUEST_STATUS_DRIVER_CANCELED,
//                    Riderequest.REQUEST_STATUS_ABORTED_UNPAID,
//                    Riderequest.REQUEST_STATUS_ON_RIDE).contains(r.getStatus())) {
//                acceptedDri.add(r.getPid());
//                avgAcceptKm += r.getExtmeters();
//                avgAccept += Long.parseLong(r.getAssigndate()) - Long.parseLong(r.getReqdate());
//                avgReimbSel += Math.max(r.getPassprice(), r.getExtprice());
//            } else {
//
//                avgStart += Long.parseLong(r.getStartdate() == null ? r.getAssigndate() : r.getStartdate()) - Long.parseLong(r.getReqdate());
//                avgEndKm += r.getExtmeters();
//                avgEndFee += r.getStripezegofee();
//                avgEndReimb += r.getDriverfee();
//                terminatedPas.add(r.getPid());
//                terminatedDri.add(pas.size());
//                loweredEndPass = loweredEndPass + ((r.getPassprice() < r.getExtprice()) ? 1 : 0);
//                loweredEndDrv = loweredEndDrv + ((r.getDriverprice() > 0 && (r.getDriverprice() < r.getExtprice()) && r.getDiscount() <= 0) ? 1 : 0);
//            }
//
//            i++;
//        }
//
//        sb.append("RICHIESTE TOTALI;").append(rr.size()).append("\n");
//        sb.append("RICHIESTE TOTALI VERE;").append(realreq).append("\n");
//        sb.append("PASSEGGERI UNICI;").append(pas.size()).append("\n\n");
//
//        sb.append("KM Medi Richiesta;").append(v(NF.format(avgKm / (rr.size() * 1000.d)))).append("\n");
//        sb.append("MEDIA RIMBORSO SUGGERITO;").append(v(NF.format(avgReimbSugg / (100 * rr.size())))).append("\n");
//        sb.append("MEDIA RIMBORSO SELEZIONATO PASS;").append(v(NF.format(avgReimbPass / (100 * rr.size())))).append("\n");
//        sb.append("Numero ride con rimborso abbassato;").append(v(NF.format(loweredPass))).append("\n\n");
//
//        /*CANCELLATE O EXPIRED
//         no drivers
//         cancellate prima dell'accettazione
//         expired
//         Altri stati (es idle o vari)
//         */
//        Integer exctr = states.get(Riderequest.REQUEST_STATUS_NO_DRIVERS) + states.get(Riderequest.REQUEST_STATUS_PASSENGER_CANCELED)
//                + states.get(Riderequest.REQUEST_CANCELLED_BY_SERVER) + states.get(Riderequest.REQUEST_STATUS_IDLE)
//                + states.get(Riderequest.REQUEST_STATUS_SUBMITTED);
//
//        sb.append("CANCELLATE O EXPIRED;").append(exctr).append("\n");
//        sb.append("no drivers;").append(states.get(Riderequest.REQUEST_STATUS_NO_DRIVERS)).append("\n");
//        sb.append("cancellate prima dell'accettazione;").append(states.get(Riderequest.REQUEST_STATUS_PASSENGER_CANCELED)).append("\n");
//        sb.append("expired;").append(states.get(Riderequest.REQUEST_CANCELLED_BY_SERVER)).append("\n");
//        sb.append("Altri stati (es idle o vari);").append(states.get(Riderequest.REQUEST_STATUS_IDLE)
//                + states.get(Riderequest.REQUEST_STATUS_SUBMITTED)).append("\n\n");
//
//        Integer accptCtr = states.get(Riderequest.REQUEST_STATUS_DRIVER_ABORTED) + states.get(Riderequest.REQUEST_STATUS_DRIVER_CANCELED)
//                + states.get(Riderequest.REQUEST_STATUS_PASSENGER_TERMINATED) + states.get(Riderequest.REQUEST_STATUS_ABORTED_UNPAID)
//                + states.get(Riderequest.REQUEST_STATUS_ON_RIDE);
//        /*
//         ACCETTATE
//         Driver unici ( che hanno accettato almeno un passaggio)
//         abortita dal driver
//         abortita dal passeggero
//         terminata ma rimborsata al passeggero (REFUNDED)
//         */
//        sb.append("ACCETTATE;").append(accptCtr).append("\n");
//        sb.append("Driver unici ( che hanno accettato almeno un passaggio);").append(acceptedDri.size()).append("\n");
//        sb.append("abortita dal driver;").append(states.get(Riderequest.REQUEST_STATUS_DRIVER_ABORTED)).append("\n");
//        sb.append("abortita dal passeggero;").append(states.get(Riderequest.REQUEST_STATUS_PASSENGER_ABORTED)).append("\n");
//        sb.append("terminata ma rimborsata al passeggero (REFUNDED);").append(states.get(Riderequest.REQUEST_STATUS_REFUNDED)).append("\n\n");
//
//        /*
//         Tempo medio accettazione
//         Distanza media del driver cha accettato
//         Km medi richiesta
//         MEDIA RIMBORSO SELEZIONATO
//         */
//        sb.append("Tempo medio accettazione;").append(mmss(((Double) (avgAccept / accptCtr)).intValue())).append("\n");
//        sb.append("Distanza media del driver cha accettato;").append(v(String.valueOf(avgAcceptDist / accptCtr))).append("\n");
//        sb.append("Km medi richiesta;").append(v(NF.format(avgAcceptKm / (accptCtr * 1000.d)))).append("\n");
//        sb.append("MEDIA RIMBORSO SELEZIONATO;").append(v(NF.format(avgReimbSel / (100 * accptCtr)))).append("\n\n");
//
//        Integer endCtr = states.get(Riderequest.REQUEST_STATUS_ENDED) + states.get(Riderequest.REQUEST_STATUS_PAID)
//                + states.get(Riderequest.REQUEST_PAYMENT_FAILED) + states.get(Riderequest.REQUEST_STATUS_PASSENGER_TERMINATED);
//        /*
//         TERMINATE
//         Tempo medio di inizio viaggio da richiesta
//         terminata (stato 9)
//         terminate e pagate (stato 10)
//         terminate dal passeggero (11)
//         terminate ma pagamento fallito (stato 12)
//         */
//
//        sb.append("TERMINATED;").append(endCtr).append("\n");
//        sb.append("Tempo medio di inizio viaggio da richiesta;").append(0).append("\n");
//        sb.append("terminata;").append(states.get(Riderequest.REQUEST_STATUS_ENDED)).append("\n");
//        sb.append("terminate e pagate;").append(states.get(Riderequest.REQUEST_STATUS_PAID)).append("\n");
//        sb.append("terminate dal passeggero;").append(states.get(Riderequest.REQUEST_STATUS_PASSENGER_TERMINATED)).append("\n");
//        sb.append("terminate ma pagamento fallito;").append(states.get(Riderequest.REQUEST_PAYMENT_FAILED)).append("\n\n");
//
//        /*
//         PASSEGGERI UNICI
//         utilizzi per passeggero unico (totale terminate/pass unico)
//         Driver unici (che hanno terminato almeno un passaggio)
//         */
//        sb.append("utilizzi per passeggero unico (totale terminate/pass unico);").append(v(NF.format(endCtr / (0.d + terminatedPas.size())))).append("\n");
//        sb.append("Driver unici (che hanno terminato almeno un passaggio);").append(terminatedDri.size()).append("\n");
//
//        /* 
//         Km medi terminate
//         MEDIA RIMBORSO SELEZIONATO
//         MEDIA FEE ZEGO
//         Numero ride con rimborso abbassato dal pass
//         Numero ride con rimborso abbassato dal driver
//         */
//        sb.append("Km medi terminate;").append(v(NF.format(avgEndKm / (endCtr * 1000.d)))).append("\n");
//        sb.append("MEDIA RIMBORSO SELEZIONATO;").append(v(NF.format(avgEndReimb / (100 * endCtr)))).append("\n");
//        sb.append("MEDIA FEE ZEGO;").append(v(String.valueOf(avgEndFee / (100 * endCtr)))).append("\n");
//        sb.append("Numero ride con rimborso abbassato dal pass;").append(loweredEndPass).append("\n");
//        sb.append("Numero ride con rimborso abbassato dal driver;").append(loweredEndDrv).append("\n");
//
//        return sb;
//    }
//    
    private String v(String p) {
        return p == null ? p : p.replace(".", ",");
    }

    private String n(String s) {
        return s == null ? "" : s;
    }

    private Integer s(Integer s) {
        return s == null ? 0 : s;
    }

    private String p(Integer p) {
        return (p == null ? "0.00" : NF.format(p / 100d)).replace(".", ",");
    }

    private String k(Integer p) {
        return (p == null ? "0.00" : NF.format(p / 1000d)).replace(".", ",");
    }

    private String mmss(Integer h) {
        Integer m = h / 60;
        Integer s = h % 60;
        return (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
    }

    private String ls(Integer l) {
        if (l.equals(RiderequestDrivers.RIDEREQUESTDRIVERS_REJECTED)) {
            return "REJECT";
        } else if (l.equals(RiderequestDrivers.RIDEREQUESTDRIVERS_ACCEPTED_BY_OTHER)) {
            return "ACCEPTED_BY_OTHERS";
        } else if (l.equals(RiderequestDrivers.RIDEREQUESTDRIVERS_CANCELED)) {
            return "CANCELED";
        } else if (l.equals(RiderequestDrivers.RIDEREQUESTDRIVERS_ACCEPTED)) {
            return "ACCEPTED";
        } else {
            return "";
        }
    }

    private String b(Integer burnt) {
        if (burnt == null || burnt.equals(0)) {
            return "NOT USED";
        } else if (burnt.equals(1)) {
            return "USED";
        } else if (burnt.equals(2)) {
            return "UNUSABLE";
        } else {
            return "";
        }
    }

    private String tz(String t) {
        return t == null ? "" : String.valueOf(Long.parseLong(t) + (offsetMillis / 1000));
    }

    @Override
    public StringBuilder fullridefixed(StringBuilder sb, String start, String stop, Integer city) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
