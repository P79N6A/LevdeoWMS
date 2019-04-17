$(document).ready(function () {
  //初始化日历
  laydate.render({
    elem: '#divCalendar',
    position: 'static',
    showBottom: false
  });

  //饼状图
  var myChart2 = echarts.init(document.getElementById("pieChart"));
  // 指定图表的配置项和数据
  var option2 = {
    title: {
      text: "成品库在库信息",
      x: "center"
    },
    legend: {
      orient: "vertical",
      x: "left",
      data: ["雷丁成品库", "借用仓库", "非商品仓库", "大客户成品仓库", "比德文成品库", "宝路达成品库", "多品牌成品库"]
    },
    calculable: true,
    series: [{
      name: "成品库信息来源",
      type: "pie",
      radius: "55%",
      center: ["50%", "60%"],
      itemStyle: {
        normal: {
          label: {
            show: true,
            formatter: "{b}: {c} ({d}%)"
          }
        }
      },
      data: [{
        value: 100,
        name: "雷丁成品库",
        itemStyle: {
          normal: {
            color: "#47D282" //图标颜色
          }
        },
      }, {
        value: 200,
        name: "借用仓库",
        itemStyle: {
          normal: {
            color: "#5f6aad" //图标颜色
          }
        },
      }, {
        value: 300,
        name: "非商品仓库",
        itemStyle: {
          normal: {
            color: "#FF5B57" //图标颜色
          }
        },
      }, {
        value: 400,
        name: "大客户成品仓库",
        itemStyle: {
          normal: {
            color: "#F59C1B" //图标颜色
          }
        },
      }, {
        value: 500,
        name: "比德文成品库",
        itemStyle: {
          normal: {
            color: "#FF5B57" //图标颜色
          }
        },
      }, {
        value: 600,
        name: "宝路达成品库",
        itemStyle: {
          normal: {
            color: "#348FE8" //图标颜色
          }
        },
      }, {
        value: 700,
        name: "多品牌成品库",
        itemStyle: {
          normal: {
            color: "#5f6aaa" //图标颜色
          }
        },
      }]
    }]
  };
  // 使用刚指定的配置项和数据显示图表。
  myChart2.setOption(option2);
})