<template>
  <div class="page-content">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="8" :sm="24">
            <a-form-item label="应用">
              <a-select v-model="app" placeholder="请选择" @change="changeApp">
                <a-select-option v-for="(item, index) in appList" :key="index" :value="item">{{item}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24">
            <a-form-item label="机器">
              <a-select v-model="queryParam.ip" placeholder="请选择" default-value="0" @change="changeMachine">
                <a-select-option v-for="(item) in machineList" :key="item.ip" :value="item.ip+':'+item.port">{{item.ip}}:{{item.port}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24">
            <span class="table-page-search-submitButtons">
              <a-button type="primary" @click="$refs.table.refresh(true)">查询</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div>
      <s-table
      ref="table"
      size="default"
      :columns="columns"
      :data="loadData"
      :showPagination="false"
      :onLoadData="false"
      >
        <span slot="action" slot-scope="text, record">
          <template>
            <a @click="handleFlow(record)">流控</a>
          </template>
        </span>
      </s-table>
    </div>
    <FlowRuleAddModal @ok="$refs.table.refresh(true)" ref="flowRuleAddModal" />
  </div>
</template>
<script>
import STable from '@/components/Table'
import { getIdentity, listApps, getMachineList } from '@/api/home'
import FlowRuleAddModal from './FlowRuleAddModal'

export default {
  name: 'Identity',
  components: {
    STable,
    FlowRuleAddModal
  },
  data () {
    return {
      data: {},
      app: '',
      appList: [],
      machineList: [],
      queryParam: {
        'type': 'cluster'
      },
      columns: [
        {
          title: '资源名',
          dataIndex: 'resource',
          width: 720
        }, {
          title: '通过QPS',
          dataIndex: 'passQps',
          width: 100
        }, {
          title: '拒绝QPS',
          dataIndex: 'blockQps',
          width: 100
        }, {
          title: '线程数',
          dataIndex: 'threadNum',
          width: 100
        }, {
          title: '平均RT',
          dataIndex: 'averageRt',
          width: 100
        }, {
          title: '分钟通过',
          dataIndex: 'oneMinutePass',
          width: 100
        }, {
          title: '分钟拒绝',
          dataIndex: 'oneMinuteBlock',
          width: 100
        }, {
          title: '操作',
          dataIndex: '',
          fixed: 'right',
          width: 100,
          scopedSlots: { customRender: 'action' }
        }
      ],
      loadData: parameter => {
        this.queryParam.app = this.app
        return getIdentity(Object.assign(parameter, this.queryParam))
          .then(res => {
            return res
          })
      }
    }
  },
  methods: {
    changeApp (app) {
      this.getMachineList()
    },
    getAppList () {
       listApps().then(res => {
         if (res.success) {
           this.appList = res.data
           this.app = res.data[0]
           this.getMachineList()
         }
       })
     },
     getMachineList () {
      getMachineList(this.app).then(res => {
        if (res.success) {
          this.machineList = res.data
          this.queryParam.ip = res.data[0].ip
          this.queryParam.port = res.data[0].port
          this.$refs.table.refresh(true)
        }
      })
     },
     changeMachine (val, option) {
       const ipPort = val.split(':')
       this.queryParam.ip = ipPort[0]
       this.queryParam.port = ipPort[1]
       this.$refs.table.refresh(true)
     },
     handleFlow (record) {
       this.$refs.flowRuleAddModal.showModal({
         'app': this.queryParam.app,
         'resource': record.resource
       })
     }
  },
  mounted () {
    this.getAppList()
  }
}
</script>
