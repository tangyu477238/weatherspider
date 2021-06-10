package cn.zifangsky.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
@Table(name = "jsl_kzz")
public class JslKzz implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonProperty("bond_id")
    @Column(length = 100)
    private String bondId;
    @JsonProperty("bond_nm")
    @Column(length = 100)
    private String bondNm;
    @JsonProperty("stock_id")
    @Column(length = 100)
    private String stockId;
    @JsonProperty("stock_nm")
    @Column(length = 100)
    private String stockNm;
    @Column(length = 100)
    private String btype;


    @JsonProperty("convert_price")
    @Column(length = 100)
    private String convertPrice;
    @JsonProperty("convert_price_valid_from")
    @Column(length = 100)
    private String convertPriceValidFrom;
    @JsonProperty("convert_dt")
    @Column(length = 100)
    private String convertDt;
    @JsonProperty("maturity_dt")
    @Column(length = 100)
    private String maturityDt;
    @JsonProperty("next_put_dt")
    @Column(length = 100)
    private String nextPutDt;

    @JsonProperty("put_dt")
    @Column(length = 100)
    private String putDt;
    @JsonProperty("put_notes")
    @Column(length = 100)
    private String putNotes;
    @JsonProperty("put_price")
    @Column(length = 100)
    private String putPrice;
    @JsonProperty("put_inc_cpn_fl")
    @Column(length = 100)
    private String putIncCpnFl;
    @JsonProperty("put_convert_price_ratio")
    @Column(length = 100)
    private String putConvertPriceRatio;

    @JsonProperty("put_count_days")
    private int putCountDays;
    @JsonProperty("put_total_days")
    private int putTotalDays;
    @JsonProperty("put_real_days")
    private int putRealDays;
    @JsonProperty("repo_discount_rt")
    @Column(length = 100)
    private String repoDiscountRt;
    @JsonProperty("repo_valid_from")
    @Column(length = 100)
    private String repoValidFrom;


    @JsonProperty("repo_valid_to")
    @Column(length = 100)
    private String repoValidTo;
    @JsonProperty("turnover_rt")
    @Column(length = 100)
    private String turnoverRt;
    @JsonProperty("redeem_price")
    @Column(length = 100)
    private String redeemPrice;
    @JsonProperty("redeem_inc_cpn_fl")
    @Column(length = 100)
    private String redeemIncCpnFl;
    @JsonProperty("redeem_price_ratio")
    @Column(length = 100)
    private String redeemPriceRatio;


    @JsonProperty("redeem_count_days")
    private int redeemCountDays;
    @JsonProperty("redeem_total_days")
    private int redeemTotalDays;
    @JsonProperty("redeem_real_days")
    private int redeemRealDays;
    @JsonProperty("redeem_dt")
    @Column(length = 100)
    private String redeemDt;
    @JsonProperty("redeem_flag")
    @Column(length = 100)
    private String redeemFlag;

    @JsonProperty("orig_iss_amt")
    @Column(length = 100)
    private String origIssAmt;
    @JsonProperty("curr_iss_amt")
    @Column(length = 100)
    private String currIssAmt;
    @JsonProperty("rating_cd")
    @Column(length = 100)
    private String ratingCd;
    @JsonProperty("issuer_rating_cd")
    @Column(length = 100)
    private String issuerRatingCd;
    @Column(length = 100)
    private String guarantor;


    @JsonProperty("ssc_dt")
    @Column(length = 100)
    private String sscDt;
    @JsonProperty("esc_dt")
    @Column(length = 100)
    private String escDt;
    @JsonProperty("sc_notes")
    @Column(length = 100)
    private String scNotes;
    @JsonProperty("force_redeem")
    @Column(length = 100)
    private String forceRedeem;
    @JsonProperty("real_force_redeem_price")
    @Column(length = 100)
    private String realForceRedeemPrice;


    @JsonProperty("convert_cd")
    @Column(length = 100)
    private String convertCd;
    @JsonProperty("repo_cd")
    @Column(length = 100)
    private String repoCd;
    @Column(length = 100)
    private String ration;
    @JsonProperty("ration_cd")
    @Column(length = 100)
    private String rationCd;
    @JsonProperty("apply_cd")
    @Column(length = 100)
    private String applyCd;

    @JsonProperty("online_offline_ratio")
    @Column(length = 100)
    private String onlineOfflineRatio;
    @Column(length = 100)
    private String qflag;
    @Column(length = 100)
    private String qflag2;
    @JsonProperty("ration_rt")
    @Column(length = 100)
    private String rationRt;
    @JsonProperty("fund_rt")
    @Column(length = 100)
    private String fundRt;
    @JsonProperty("margin_flg")
    @Column(length = 100)
    private String marginFlg;






    @Column(length = 100)
    private String pb;
    @JsonProperty("pb_flag")
    @Column(length = 100)
    private String pbFlag;
    @JsonProperty("total_shares")
    @Column(length = 100)
    private String totalShares;
    @Column(length = 100)
    private String sqflg;
    @Column(length = 100)
    private String sprice;

    @Column(length = 100)
    private String svolume;
    @JsonProperty("sincrease_rt")
    @Column(length = 100)
    private String sincreaseRt;
    @Column(length = 100)
    private String qstatus;
    @JsonProperty("bond_value")
    @Column(length = 100)
    private String bondValue;
    @JsonProperty("bond_value2")
    @Column(length = 100)
    private String bondValue2;

    @JsonProperty("volatility_rate")
    @Column(length = 100)
    private String volatilityRate;
    @JsonProperty("last_time")
    @Column(length = 100)
    private String lastTime;
    @JsonProperty("convert_value")
    @Column(length = 100)
    private String convertValue;
    
    @JsonProperty("premium_rt")
    @Column(length = 100)
    private String premiumRt;
    @JsonProperty("year_left")
    @Column(length = 100)
    private String yearLeft;


    @JsonProperty("ytm_rt")
    @Column(length = 100)
    private String ytmRt;
    @JsonProperty("ytm_rt_tax")
    @Column(length = 100)
    private String ytmRtTax;
    @Column(length = 100)
    private String price;
    @JsonProperty("full_price")
    @Column(length = 100)
    private String fullPrice;
    @JsonProperty("increase_rt")
    @Column(length = 100)
    private String increaseRt;


    @Column(length = 100)
    private String volume;
    @JsonProperty("convert_price_valid")
    @Column(length = 100)
    private String convertPriceValid;
    @JsonProperty("adj_scnt")
    private int adjScnt;
    @JsonProperty("adj_cnt")
    private int adjCnt;
    @JsonProperty("redeem_icon")
    @Column(length = 100)
    private String redeemIcon;


    @JsonProperty("ref_yield_info")
    @Column(length = 100)
    private String refYieldInfo;
    @JsonProperty("adjust_tip")
    @Column(length = 100)
    private String adjustTip;
    @Column(length = 100)
    private String adjusted;
    @JsonProperty("option_tip")
    @Column(length = 100)
    private String optionTip;
    @JsonProperty("bond_value3")
    @Column(length = 100)
    private String bondValue3;

    @JsonProperty("left_put_year")
    @Column(length = 100)
    private String leftPutYear;
    @JsonProperty("short_maturity_dt")
    @Column(length = 100)
    private String shortMaturityDt;
    @Column(length = 100)
    private String dblow;
    @JsonProperty("force_redeem_price")
    @Column(length = 100)
    private String forceRedeemPrice;
    @JsonProperty("put_convert_price")
    @Column(length = 100)
    private String putConvertPrice;

    @JsonProperty("convert_amt_ratio")
    @Column(length = 100)
    private String convertAmtRatio;
    @JsonProperty("stock_net_value")
    @Column(length = 100)
    private String stockNetValue;
    @JsonProperty("stock_cd")
    @Column(length = 100)
    private String stockCd;
    @JsonProperty("pre_bond_id")
    @Column(length = 100)
    private String preBondId;
    @JsonProperty("repo_valid")
    @Column(length = 100)
    private String repoValid;

    @JsonProperty("convert_cd_tip")
    @Column(length = 100)
    private String convertCdTip;
    @JsonProperty("price_tips")
    @Column(length = 100)
    private String priceTips;
}
