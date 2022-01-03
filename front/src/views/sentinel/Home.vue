<template>
  <div class="page-content">
    <div class="table-page-search-wrapper">
      <a-form layout="inline" :form="form">
        <a-form-item label="应用" :label-col="formItemLayout.labelCol" :wrapper-col="formItemLayout.wrapperCol">
          <a-select v-model="app" placeholder="请选择" @change="changeApp" style="width:250px;">
            <a-select-option v-for="(item, index) in appList" :key="index" :value="item">{{item}}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="时间">
          <a-range-picker
            :show-time="{ format: 'HH:mm' }"
            format="YYYY-MM-DD HH:mm"
            :placeholder="['开始时间', '结束时间']"
            @change="changeTime"
            @ok="onOk"
          />
        </a-form-item>
        <a-form-item>
          <a-button type="primary" :disabled="refreshSwitch">查询</a-button>
        </a-form-item>
        <a-form-item>
          <a-switch checked-children="定时刷新" un-checked-children="定时刷新" default-checked v-model="refreshSwitch"/>
        </a-form-item>
      </a-form>
    </div>
    <a-card :bordered="false" :body-style="{padding: '0',marginBottom: '10px'}" :title="item.res" v-for="(item, index) in chartData.metric" :key="index">
      <div class="salesCard">
        <div class="extra-wrapper" >
          <div :id="'container_' + index"/>
        </div>
      </div>
    </a-card>
    <a-pagination
      :current="chartData.pageIndex"
      :total="chartData.totalCount"
      :pageSize="6"
      :show-total="(total, range) => `共${total}条记录`"/>
  </div>
</template>

<script>
  import { Line } from '@antv/g2plot'
  import { listQps, listApps } from '@/api/home'
  import { Pagination } from 'ant-design-vue'
  import { timeFormat } from '@/utils/util'

  export default {
    name: 'Home',
    components: {
      APagination: Pagination
    },
    data () {
      return {
        form: this.$form.createForm(this, 'horizontal_login'),
        chartData: {},
        page: 1,
        app: '',
        appList: [],
        timer: null,
        chart: [],
        formItemLayout: {},
        queryParam: {},
        refreshSwitch: false
      }
    },
    methods: {
     changeApp (val) {
       this.app = val
       this.renderQps()
     },
     getAppList () {
       listApps().then(res => {
         if (res.success) {
           this.appList = res.data
           this.app = res.data[0]
           this.renderQps(true)
         }
       })
     },
     fillZeros (metricData) {
        if (!metricData || metricData.length === 0) {
          return []
        }
        var filledData = []
        filledData.push(metricData[0])
        var lastTime = metricData[0].timestamp / 1000
        for (var i = 1; i < metricData.length; i++) {
          var curTime = metricData[i].timestamp / 1000
          if (curTime > lastTime + 1) {
            for (var j = lastTime + 1; j < curTime; j++) {
              filledData.push({
                  'timestamp': j * 1000,
                  'passQps': 0,
                  'blockQps': 0,
                  'successQps': 0,
                  'exception': 0,
                  'rt': 0,
                  'count': 0
              })
            }
          }
          filledData.push(metricData[i])
          lastTime = curTime
        }
        return filledData
      },
      formatData (data) {
        var filledData = []
        for (var i = 1; i < data.length; i++) {
          filledData.push({
            'timestamp': data[i].timestamp,
            'category': '通过QPS',
            'value': data[i].passQps
          })
          filledData.push({
            'timestamp': data[i].timestamp,
            'category': '拒绝QPS',
            'value': data[i].blockQps
          })
        }
        return filledData
      },
      rendChart (data, i) {
        const line = new Line('container_' + i, {
          data: data,
          padding: 'auto',
          height: 260,
          xAxis: {
            type: 'timeCat',
            mask: 'HH:mm',
            tickInterval: 2
          },
          xField: 'timestamp',
          yField: 'value',
          seriesField: 'category',
          smooth: true,
          tooltip: {
            customContent: (title, value) => {
              if (value && value.length > 0) {
                const xValue = timeFormat(value[0].data.timestamp / 1000)
                var html = `<div class="tooltip-item"><div>${xValue}</div>`
                for (var item in value) {
                  const result = value[item].data
                  html += `<div><span class="item-title">${result.category}:</span><span class="item-val">${result.value}</span></div>`
                }
                html += `</div>`
              }
              return html
            }
          },
          legend: {
            layout: 'horizontal',
            position: 'bottom'
          }
        })
        line.render()
        return line
      },
      initChart () {
        this.chart = []
        for (var i = 0; i < this.chartData.metric.length; i++) {
          this.chart.push(this.rendChart(this.chartData.metric[i].data, i))
        }
      },
      changePage (page) {
        this.page = page
        this.renderQps()
      },
      changeData () {

      },
      renderQps (initChart) {
        listQps({
          'app': this.app,
          'desc': true,
          'pageIndex': this.page,
          'pageSize': 6
        }).then(res => {
          const data = res.data.metric
          this.chartData = res.data
          var metrics = []
          for (var i in data) {
            const item = data[i]
            if (item && item.length > 0) {
              const chartData = this.formatData(this.fillZeros(item))
              metrics.push({
                res: i,
                data: chartData
              })
            }
          }
          this.chartData.metric = metrics
          if (initChart) {
            this.$nextTick(res => {
              this.initChart()
              this.timer = setInterval(() => {
                  this.renderQps(false)
              }, 1000 * 10)
            })
          } else {
            for (var k = 0; k < this.chartData.metric.length; k++) {
              console.log(this.chart[k])
              if (!this.chart[k]) {
                this.chart[k] = this.rendChart(this.chartData.metric[k].data, k)
              } else {
                this.chart[k].changeData(this.chartData.metric[k].data)
              }
            }
          }
        })
      },
      clear () {
        clearInterval(this.timer)
        this.timer = null
      },
      changeTime (value, dateString) {
        console.log('Selected Time: ', value)
        console.log('Formatted Selected Time: ', dateString)
      },
      onOk (value) {
        this.queryParam.startTime = value[0].startOf('day').format('YYYY-MM-DD HH:mm:ss')
        this.queryParam.endTime = value[1].endOf('day').format('YYYY-MM-DD HH:mm:ss')
      },
      changeRefresh (value) {
        if (value) {

        }
      }
    },
    mounted () {
      this.getAppList()
    },
    destroyed () {
      this.clear()
    }
  }
</script>
<style lang="less" scoped>
  .extra-wrapper {
    line-height: 55px;
    padding-right: 24px;
    .extra-item {
      display: inline-block;
      margin-right: 24px;
      a {
        margin-left: 24px;
      }
    }
  }
  .salesCard{
    padding:10px;
  }
  .app-title{
    padding: 10px;
    background: #fff;
    border-bottom: 1px solid #eee;
    margin-bottom: 10px;
  }
  .app-list{
  }
</style>
<style lang="less">
 .tooltip-item{
    padding: 10px;
    >div{
      padding: 5px 0px;
    }
    .item-title{
      font-weight: 500;
    }
    .item-val{
      padding-left: 15px;
    }
  }
</style>
