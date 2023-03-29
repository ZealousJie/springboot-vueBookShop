<template>
  <div style="padding: 10px">
    <!--    功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add" size="medium">新增</el-button>
      <el-upload
        action="http://localhost:9090/xx/import"
        :on-success="handleUploadSuccess"
        :show-file-list="false"
        :limit="1"
        accept=".xlsx"
        style="display: inline-block; margin: 0 10px"
      >
        <el-button type="primary" size="medium">导入</el-button>
      </el-upload>
      <el-dropdown>
        <el-button type="success" size="medium"> 导出数据<i class="el-icon-arrow-down el-icon--right"></i> </el-button>
        <template #dropdown>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>导出为Xml</el-dropdown-item>
            <el-dropdown-item>导出为Excel</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入关键字" style="width: 20%"></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load()" size="medium">搜索</el-button>
      <el-button type="danger" style="margin-left: 5px" @click="batchDel" size="medium">批量删除</el-button>
    </div>
    <div style="margin: 10px 0">
      <el-select v-model="form.eventType" placeholder="赛事类型" style="margin: 10px 10px 10px 0" @change="load" clearable filterable>
        <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value"> </el-option>
      </el-select>
      <el-select v-model="form.eventProject" placeholder="赛事项目" filterable clearable style="margin: 10px 10px 10px 0" @change="load">
        <el-option v-for="(item, i) in projectOptions" :key="i" :label="item.projectName" :value="item.projectName"> </el-option>
      </el-select>
      <el-select v-model="form.organizer" placeholder="举办方" filterable clearable style="margin: 10px 10px 10px 0" @change="load">
        <el-option v-for="(item, i) in organizerOptions" :key="i" :label="item.organizerName" :value="item.organizerName"> </el-option>
      </el-select>
    </div>
    <el-table
      :data="tableData"
      style="width: 99%"
      stripe
      border
      size="18px"
      @row-dblclick="eventNews"
      :row-class-name="tableRow"
      @selection-change="handleSelectionChange"
    >
      <!--      sortable 加了个可以排序的东东 prop name label value-->
      <el-table-column type="selection" />
      <el-table-column label="序号" sortable width="75px" type="index" :index="hIndex" align="center"> </el-table-column>
      <el-table-column prop="eventName" label="赛事名称" width="100px" align="center" />
      <el-table-column prop="eventProject" label="赛事项目" width="100px" align="center" />
      <el-table-column prop="eventSpecification" label="赛事规格" width="160px" align="center" />
      <!--      下面三个被注释的移动到详情里-->
      <!--      <el-table-column prop="eventStage" label="赛事阶段" width="100px" align="center"/>-->
      <!--      <el-table-column prop="eventEntrants" label="参赛团队" width="100px" align="center">-->
      <!--      </el-table-column>-->
      <!--      <el-table-column prop="bonus" label="奖金(元)" width="80px" align="center"/>-->
      <el-table-column prop="organizer" label="举办方" width="180px" align="center" />
      <el-table-column prop="eventVenue" label="参赛地点" width="150px" show-overflow-tooltip align="center">
        <template #default="scope">
          <span v-if="scope.row.eventOnlineType === 1"> 线上赛无参赛地点 </span>
        </template>
      </el-table-column>
      <el-table-column prop="unitPrice" label="单人票价" width="140px" show-overflow-tooltip align="center">
        <template #default="scope">
          <span v-if="scope.row.eventOnlineType === 1"> 无线下票贩卖 </span>
        </template>
      </el-table-column>
      <!--      <el-table-column prop="remainingTickets" label="剩余票数" width="80px" show-overflow-tooltip align="center">-->
      <!--        <template #default="scope">-->
      <!--          <span v-if="scope.row.eventOnlineType === 1"> 无 </span>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <el-table-column fixed="right" label="操作" align="center">
        <template #default="scope">
          <el-button size="small" @click="details(scope.row)">详情</el-button>
          <el-button size="small" @click="buy(scope.row.unitPrice)" :disabled="!scope.row.unitPrice > 0">购票</el-button>
          <el-button size="small" @click="handleClick(scope.row)" class="el-icon-edit" circle></el-button>
          <el-button type="warning" class="el-icon-view" circle @click="attentionOrNot(scope.row)" />
          <el-popconfirm title="确定要删除吗" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button size="small" type="danger" class="el-icon-delete" circle></el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <!--  分页  -->
    <div style="padding: 10px 0">
      <el-pagination
        :currentPage="currentPage"
        :page-size="pageSize"
        :page-sizes="[5, 10, 20]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
      <!--  分页  -->

      <!--编辑修改弹出框-->
      <el-dialog v-model="dialogVisible" title="赛事管理" width="60%">
        <el-form ref="form" :model="form" label-width="120px" :rules="rules" size="14px">
          <el-form-item label="赛事名称" prop="eventName">
            <el-input v-model="form.eventName" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="赛事项目" prop="eventProject">
            <el-select v-model="form.eventProject" placeholder="赛事项目" filterable clearable style="margin: 10px 10px 10px 0">
              <el-option label="英雄联盟" value="英雄联盟"></el-option>
              <el-option label="CSGO" value="CSGO"></el-option>
              <el-option label="APEX" value="APEX"></el-option>
              <el-option label="足球" value="足球"></el-option>
              <el-option label="篮球" value="篮球"></el-option>
              <el-option label="炉石传说" value="炉石传说"></el-option>
              <el-option label="单人乒乓" value="单人乒乓"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="预计举办时间" prop="eventDuration" v-if="this.flag">
            <el-date-picker
              v-model="form.eventDuration"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            >
            </el-date-picker>
          </el-form-item>
          <el-form-item label="赛事演出形式" prop="eventOnlineType">
            <el-radio-group v-model="form.eventOnlineType" @change="agreeChange">
              <el-radio label="0" border>仅线下</el-radio>
              <el-radio label="1" border>仅线上</el-radio>
              <el-radio label="2" border>线上下均有</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="赛事举办地点" prop="eventVenue" v-if="isShow">
            <el-input v-model="form.eventVenue" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="单人票价" prop="eventVenue" v-if="isShow">
            <el-input v-model="form.unitPrice" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="举办方" prop="organizer">
            <el-input v-model="form.organizer" style="width: 80%"></el-input>
          </el-form-item>
        </el-form>

        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="save">确认</el-button>
          </span>
        </template>
      </el-dialog>
      <el-dialog v-model="vis" title="赛事详情" width="50%">
        <el-form :model="detail" label-width="120px">
          <el-form-item label="赛事阶段" size="18px">
            <el-input v-model="this.detail.eventStage" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="参赛团队" size="18px">
            <el-dropdown>
              <el-button type="success" size="medium"> 参赛列表<i class="el-icon-arrow-down el-icon--right"></i> </el-button>
              <template #dropdown>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item v-for="item in detail.eventEntrants">{{ item.teamName }}</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </el-form-item>
          <el-form-item label="赛事奖金" v-if="this.detail.bonus != null" size="18px">
            <el-input v-model="this.detail.bonus" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="剩余票数" v-if="!this.detail.eventOnlineType === 1" size="18px">
            <el-input v-model="this.detail.remainingTickets" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item v-for="item in detail.attributeList" :label="item.attributeName" size="18px">
            <el-input v-model="item.attributeValue" style="width: 80%"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="vis = false">确认</el-button>
          </span>
        </template>
      </el-dialog>
      <el-dialog v-model="buyVis" title="购票详情" width="50%">
        <el-form :model="buyMsg" label-width="120px">
          <el-form-item label="购票数量" size="18px">
            <el-input type="number" @change="priceCreate(this.buyMsg.number)" v-model="this.buyMsg.number" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="购票金额" size="18px">
            <el-input v-model="this.buyMsg.price" style="width: 80%" disabled></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="sureBuy()">确认</el-button>
          </span>
        </template>
      </el-dialog>
      <el-dialog v-model="visEvent" title="购票详情" width="600px">
        <div ref="graph"></div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import request from '../utils/request';
import E from 'wangeditor';
import G6 from '@antv/g6';

let editor;
export default {
  name: 'Event',
  components: {},
  data() {
    return {
      searchForm: {},
      ids: [],
      buyVis: false,
      buyMsg: {},
      unitPrice: 0,
      tableChecked: [],
      form: {},
      isShow: false,
      attributeForm: {},
      dialogVisible: false,
      vis: false,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      search: '',
      detail: {},
      flag: true,
      typeOptions: [
        {
          value: 1,
          label: '电子竞技',
        },
        {
          value: 0,
          label: '体育竞技',
        },
      ],
      projectOptions: [],
      organizerOptions: [],
      // filesUploadUrls: "http://" + window.server.filesUploadUrl + ":9090/files/upload",
      tableData: [],
      visEvent: false,
    };
  },
  created() {
    // 页面加载时就执行的方法
    this.load();
  },
  methods: {
    priceCreate(number) {
      this.buyMsg.price = number * this.unitPrice;
    },
    buy(price) {
      this.buyVis = true;
      this.unitPrice = price;
    },
    details(row) {
      this.vis = true;
      this.detail = row;
    },
    attentionOrNot(row) {
      let attentionForm = null;
      if (row.eventAttentionState == 'IS_ATTENTION') {
        attentionForm = {
          typeId: 0,
          eventId: row.id,
        };
      } else {
        attentionForm = {
          typeId: 1,
          eventId: row.id,
        };
      }
      request.post('/event/attentionOrNot', attentionForm).then((res) => {
        if (res.code === '0') {
          //element ui 提供的提示框 别忘了这个美元符合
          this.$message({
            type: 'success',
            message: '切换成功',
          });
          this.load();
        } else {
          this.$message({
            type: 'error',
            message: res.msg,
          });
        }
      });
    },
    sureBuy() {
      request.post('/event/attentionOrNot', attentionForm);
      this.buyVis = false;
      this.buyMsg = {};
    },
    agreeChange(val) {
      this.isShow = val === '0' || val === '2' ? true : false;
      console.log(this.isShow);
    },
    hIndex(index) {
      // index索引从零开始，index +1即为当前数据序号
      this.currentPage <= 0 ? (this.currentPage = 1) : this.currentPage;
      // 如果当前不是第一页数据
      if (this.currentPage != 1) {
        // index + 1 + (( 当前页 - 1) * 每页展示条数)
        // 比如是第二页数据 1 + ((2 - 1)*5) = 6,第二页数据也就是从序号6开始
        return index + 1 + (this.currentPage - 1) * this.pageSize;
      }
      // 否则直接返回索引+1作为序号
      return index + 1;
    },
    handleUploadSuccess(res) {
      if (res.code === '0') {
        this.$message.success('导入成功');
        this.load();
      }
    },
    exportEvent() {
      location.href = 'http://' + window.server.filesUploadUrl + ':9090/xx/export';
    },
    createEditor() {
      editor = new E('#div1');
      //因为这个富文本编辑器对返回的数据类型有严格要求，重新写一个接口
      editor.config.uploadImgServer = 'http://localhost:9090/files/editor/upload';
      editor.config.uploadFileName = 'file';
      editor.create();
    },
    eventNews() {
      this.visEvent = true;
      this.graph = new G6.Graph({
        container: this.$refs.graph, // String | HTMLElement，必须，在 Step 1 中创建的容器 id 或容器本身
        width: 600, // Number，必须，图的宽度
        height: 500, // Number，必须，图的高度
      });
      this.graph.data({
        // 点集
        nodes: [
          {
            id: 'node1',
            x: 100,
            y: 200,
          },
          {
            id: 'node2',
            x: 300,
            y: 200,
          },
        ],
        // 边集
        edges: [
          // 表示一条从 node1 节点连接到 node2 节点的边
          {
            source: 'node1',
            target: 'node2',
          },
        ],
      }); // 读取 Step 2 中的数据源到图上
      this.graph.render();
    },
    tableRow({ row }) {
      if (row.eventAttentionState == 'IS_ATTENTION') {
        return 'light-row';
      }
    },
    //查询方法
    load() {
      this.form.pageNum = this.currentPage;
      this.form.pageSize = this.pageSize;
      this.form.eventName = this.search;
      request.post('/event/queryAllEvents', this.form).then((res) => {
        this.tableData = res.data.list;
        this.total = res.data.total;
      });
      request.post('/event/queryAllProjectName').then((res) => {
        this.projectOptions = res.data;
      });
      request.post('/event/queryAllOrganizerName').then((res) => {
        this.organizerOptions = res.data;
      });
    },
    add() {
      this.flag = true;
      this.dialogVisible = true;
      this.form = {}; //清空表单域
    },
    handleSelectionChange(val) {
      this.tableChecked = val;
    },
    batchDel() {
      this.ids = [];
      if (this.tableChecked.length <= 0) {
        //选中0条数据时
        this.$message.info('未选中数据');
        return;
      }
      this.tableChecked.forEach((element) => {
        //选中多条数据时
        this.ids.push(element.id);
      });
      let params = {
        ids: this.ids,
      };
      this.delete(params);
    },
    delete(params) {
      request.post('/event/deleteEvent', params).then((res) => {
        if (res.code === '0') {
          //element ui 提供的提示框
          this.$message({
            type: 'success',
            message: '删除成功',
          });
        } else {
          this.$message({
            type: 'error',
            message: res.msg,
          });
        }
        this.load();
      });
    },
    save() {
      if (this.form.id) {
        //update
        request.post('/event/updateEvent', this.form).then((res) => {
          console.log(res);
          if (res.code === '0') {
            //element ui 提供的提示框 别忘了这个美元符合
            this.$message({
              type: 'success',
              message: '更新成功',
            });
          } else {
            this.$message({
              type: 'error',
              message: res.msg,
            });
          }
          this.form = {};
          this.load();
          this.dialogVisible = false;
        });
      } else {
        //add
        request.post('/event/addEvent', this.form).then((res) => {
          if (res.code === '0') {
            //element ui 提供的提示框
            this.$message({
              type: 'success',
              message: '新增成功',
            });
          } else {
            this.$message({
              type: 'error',
              message: res.msg,
            });
          }
          this.form = {};
          this.load();
          this.dialogVisible = false;
        });
      }
    },

    //编辑
    handleClick(row) {
      this.flag = false;
      this.form = row;
      this.dialogVisible = true;
    },
    handleDelete(id) {
      this.ids = [];
      this.ids.push(id);
      let params = {
        ids: this.ids,
      };
      request.post('/event/deleteEvent', params).then((res) => {
        if (res.code === '0') {
          //element ui 提供的提示框
          this.$message({
            type: 'success',
            message: '删除成功',
          });
        } else {
          this.$message({
            type: 'error',
            message: res.msg,
          });
        }
        this.load();
      });
    },
    handleSizeChange(ps) {
      this.pageSize = ps;
      this.load();
    },
    handleCurrentChange(pn) {
      this.currentPage = pn;
      this.load();
    },
  },
};
</script>
<style>
.el-table .light-row {
  color: #ffa07a;
}
</style>
