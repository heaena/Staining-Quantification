<template>
  <a-card :bordered="false">
    <a-row>
      <a-col :span="22">
        <div>
          <strong>任务名称：</strong>{{ this.dataSource.taskName }}，任务时间：{{ formatDate(this.dataSource.taskTime) }}
        </div>
        <div style="margin-bottom: 5px;">
          <span style="color: red;">图片文件地址：</span>{{ this.dataSource.canonicalPath }}
        </div>
      </a-col>
      <a-col :span="2">
        <a-button style="margin-left: 8px" @click="() => this.$router.push({ path: '/tasks' })">全部任务</a-button>
      </a-col>
    </a-row>

    <!--操作按钮-->
    <div>
      <a-row>
        <a-col :span="24">
          <a-form layout="inline">
            <a-form-item label="源文件名">
              <a-input v-model="queryParam.filterName" placeholder="源文件名"/>
            </a-form-item>
            <a-form-item>
              <a-button @click="loadData()">查询</a-button>
              <a-button style="margin-left: 8px" @click="() => this.queryParam = {}">重置</a-button>
              <a-button style="margin-left: 8px" type="primary" @click="() => this.analysisResultData.showModal = true">分析结果数据</a-button>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
    </div>
    <!--文件列表-->
    <a-row>
      <a-col :span="6">
        <a-table
          :data-source="tableDataSource"
          :pagination="true"
          size="small">
          <a-table-column key="name" title="源文件名" data-index="name">
            <template slot-scope="text, record">
              <a @click="onClickSourceImageName(record.name, record.resourcePath, record.canonicalFilePath)" style="font-size: 16px;width: 100%;display: block;">
                <a-icon type="file-text" style="fontSize: 20px;margin-right: 10px;" theme="filled" /><span style="color: black">{{ record.name }}</span>
              </a>
            </template>
          </a-table-column>
        </a-table>
      </a-col>
      <a-col :span="18">
        <div style="padding-left: 10px;">
          <a-row>
            <a-col v-for="image in imageDetail" :key="image.name" :span="12">
              <a-card hoverable>
                <strong>{{ image.name }}</strong>
                <img :src="getImagePath(image.resourcePath)" style="width: 100%"/>
              </a-card>
            </a-col>
          </a-row>
        </div>
      </a-col>
    </a-row>
    <!--图片详情-->
    <a-drawer
      :title="image.name"
      placement="left"
      :closable="true"
      :maskClosable="false"
      :visible="image.showModal"
      width="90%"
      @close="handleImageModalCancel"
    >
      <a :href="image.resourcePath" :download="image.name" style="font-size: 20px;border: silver 1px;">下载</a>
      <span style="margin-left: 20px;">文件路径 {{ image.canonicalFilePath }}</span>
      <img :src="image.resourcePath" width="100%"/>
    </a-drawer>

    <a-drawer
      title="分析结果数据"
      placement="right"
      :closable="true"
      :maskClosable="false"
      :visible="analysisResultData.showModal"
      width="auto"
      @close="handleAnalysisResultDataCancel"
    >
      <span style="color: red;">数据文件地址：</span>{{ this.dataSource.canonicalPath }}/out_stats/out_stats_all.csv
      <table v-if="dataSource.dataJson" border="1" class="sample-table">
        <tr v-for="(dataRow, a) in dataSource.dataJson" :key="a">
          <td v-for="(dataCol, j) in dataRow" :key="j">{{ dataCol }}</td>
        </tr>
      </table>
    </a-drawer>
  </a-card>
</template>
<script>
import { getTaskResult } from '@/api/analysisTasks.js'
import Moment from 'moment'
export default {
  name: 'TableList',
  data () {
    return {
      collapseActiveKey: ['1'],
      fileList: [],
      image: {
        name: '',
        showModal: false,
        scr: ''
      },
      analysisResultData: {
        showModal: false
      },
      confirmLoading: false,
      // 查询参数
      queryParam: {
        filterName: ''
      },
      dataSource: {},
      tableDataSource: [],
      imageDetail: []
    }
  },
  filters: {
  },
  created () {
    this.loadData()
  },
  computed: {
  },
  methods: {
    str2JSON (str) {
      return JSON.parse(str)
    },
    formatDate (value) {
      return Moment(value).format('YYYY-MM-DD HH:mm:ss')
    },
    onClickSourceImageName (name) {
      this.imageDetail = this.dataSource.imageMap[name]
      console.log(this.imageDetail)
    },
    onClickImage (name, resourcePath, canonicalFilePath) {
      this.image.name = name
      this.image.resourcePath = resourcePath
      this.image.canonicalFilePath = canonicalFilePath
      this.image.showModal = true
    },
    handleImageModalCancel (e) {
      this.image.showModal = false
    },
    handleAnalysisResultDataCancel () {
      this.analysisResultData.showModal = false
    },
    getImagePath (path) {
      return path
    },
    loadData (callback) {
      const requestParameters = Object.assign({ taskName: this.$route.params.taskName }, this.queryParam)
      getTaskResult(requestParameters)
        .then(res => {
          this.dataSource = res.data
          if (res.data.imageMap) {
            const tableDataSource = []
            Object.keys(res.data.imageMap).forEach(name => {
              tableDataSource.push({ name: name })
            })
            this.tableDataSource = tableDataSource
          }
          if (res.data.data) {
            this.dataSource.dataJson = this.str2JSON(res.data.data)
          }
          if (callback) {
            callback()
          }
        })
    }
  }
}
</script>
<style scoped lang="less">
.sample-table {
  td {
    text-align: left;
    padding: 5px;
  }
}
</style>
