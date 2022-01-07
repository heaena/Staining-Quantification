<template>
  <div class="page-content">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="8" :sm="24">
            <a-form-item label="应用">
              <a-select v-model="app" placeholder="请选择" @change="changeApp">
                <a-select-option v-for="item in appList" :key="item" :value="item">{{item}}</a-select-option>
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
    <div class="table-operator">
        <a-button type="primary" icon="plus" @click="handleAdd">新建规则</a-button>
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
            <a @click="handleFlow(record)">编辑</a>
            <a-divider type="vertical" />
            <a-popconfirm
              title="确定要删除吗?"
              @confirm="() => delFlow(record)"
            >
              <a href="javascript:;">删除</a>
            </a-popconfirm>
          </template>
        </span>
      </s-table>
    </div>
    <FlowRuleAddModal ref="flowRuleAddModal"  @ok="$refs.table.refresh(true)" />
  </div>
</template>
<script>
import STable from '@/components/Table'
import { listRule, listApps, delRule } from '@/api/home'
import FlowRuleAddModal from './FlowRuleAddModal'

export default {
  name: 'RuleList',
  components: {
    STable,
    FlowRuleAddModal
  },
  data () {
    return {
      strategyList: {
        0: '直接',
        1: '关联',
        2: '链路'
      },
      gradeList: {
        0: '线程数',
        1: 'QPS'
      },
      controlBehaviorList: {
        0: '快速失败',
        2: '排队等待'
      },
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
          title: '来源应用',
          dataIndex: 'limitApp',
          width: 100
        }, {
          title: '流控模式',
          dataIndex: 'strategy',
          customRender: data => this.strategyList[data],
          width: 100
        }, {
          title: '阈值类型',
          dataIndex: 'grade',
          width: 100,
          customRender: data => this.gradeList[data]
        }, {
          title: '阈值',
          dataIndex: 'count',
          width: 100
        }, {
          title: '阈值模式',
          dataIndex: 'clusterMode',
          customRender: data => data ? '集群' : '单机',
          width: 100
        }, {
          title: '流控效果',
          dataIndex: 'controlBehavior',
          customRender: data => this.controlBehaviorList[data],
          width: 100
        }, {
          title: '操作',
          dataIndex: '',
          scopedSlots: { customRender: 'action' },
          width: 120
        }
      ],
      loadData: parameter => {
        this.queryParam.app = this.app
        return listRule(Object.assign(parameter, this.queryParam))
          .then(res => {
            return res
          })
      }
    }
  },
  methods: {
    changeApp (app) {
      this.$refs.table.refresh(true)
    },
    getAppList () {
       listApps().then(res => {
         if (res.success) {
           this.appList = res.data
           this.app = res.data[0]
           this.$refs.table.refresh(true)
         }
       })
     },
     handleAdd () {
       this.$refs.flowRuleAddModal.showModal({
         'app': this.queryParam.app
       })
     },
     handleFlow (record) {
       this.$refs.flowRuleAddModal.showModal({
         'id': record.id,
         'app': this.queryParam.app,
         'resource': record.resource,
         'limitApp': record.limitApp,
         'grade': record.grade,
         'count': record.count,
         'strategy': record.strategy,
         'controlBehavior': record.controlBehavior,
         'maxQueueingTimeMs': record.maxQueueingTimeMs
       })
     },
     delFlow (record) {
      delRule(record.id).then(res => {
        if (res.success) {
          this.$message.success('删除成功')
          this.$refs.table.refresh(true)
        } else {
          this.$message.error('删除失败')
        }
      })
     }
  },
  mounted () {
    this.getAppList()
  }
}
</script>
