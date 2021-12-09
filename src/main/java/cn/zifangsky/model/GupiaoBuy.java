package cn.zifangsky.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "gupiao_buy")
public class GupiaoBuy implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String symbol;
    private String name;

    private double amount;
    private double volume;
    private double open;
    private double high;
    private double low;
    private double close;

    //振幅(百分比)
    private double ps;
    //深/沪
    private int type;
    //涨幅(百分比)
    private double percent;
    //创建时间
    private String dividend_yield;
    private double chg;

    private double net_profit_cagr;
    private boolean has_follow;
    private double tick_size;
    private double pb_ttm;
    private long float_shares;
    private int current;
    private double amplitude;
    private double pcf;
    private double current_year_percent;
    private long float_market_capital;
    private long market_capital;
    private int lot_size;
    private double roe_ttm;
    private double total_percent;
    private double percent5m;
    private double income_cagr;
    private long issue_date_ts;
    private int main_net_inflows;
    private String volume_ratio;
    private double pb;
    private int followers;
    private double turnover_rate;
    private double first_percent;

    private double pe_ttm;
    private long total_shares;
}
