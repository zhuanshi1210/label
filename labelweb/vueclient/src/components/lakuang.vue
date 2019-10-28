<<template>
  <div class="mycanvas-container">
    <el-container  style=" border: 1px solid #eee">
      <el-header style="text-align: center; font-size: 35px">LINKE数据标注平台</el-header>
      <el-main>
        <el-row>
          <el-col :span="8">
    <div class="left">
      <p>展示视口</p>
      <div class="myshow">
        <img :src="mysrc" alt width="100%" />
      </div>
    </div>
          </el-col>
          <el-col :span="8">
    <div class="center">
      <p>操作视口</p>
      <div class="myedit" ref="myedit" @mousedown.prevent="onMousedown" @mousemove="onMousemove" @mouseup="onMouseup" @contextmenu.prevent="">
        <img :src="mysrc" />
        <div  v-for="(item, index) in mydata">
         <el-tooltip class="item" effect="dark" :content="item.label" placement="top">
        <span
          class="myedit-span"
          :key="index"
          :style="getSpanStyle(item)"
          @contextmenu.prevent="onContextmenu(item,$event)"
        >
        </span>
          </el-tooltip>
        </div>
      </div>
    </div>
          </el-col>
          <el-col :span="8">
            <div class="right">
              <input v-show="0" ref="file" type="file" class="mybutton" @change="onChange" />
              <el-button class="asideButton" @click="selectFile">导入图片</el-button>
              <el-button style="margin-top: 20px;margin-left:0px">保存图片</el-button>
            </div>
          </el-col>
        </el-row>
        <div class="myMenu" v-show="currentItem" :style="mymenu.style">
          <el-row><el-col :span="24">
            <el-input v-show="currentItem" v-model="labelContent" placeholder="此处显示标签"></el-input>
          </el-col></el-row>
          <el-row><el-col :span="24">
            <el-cascader
              v-model="value"
              :options="options"
              :props="{ expandTrigger: 'hover' }"
              @change="handleChange">
            </el-cascader>
          </el-col></el-row>
          <el-row><el-col :span="24">
            <el-button type="primary" @click="addLabel">确认</el-button>
          </el-col></el-row>
          <el-row><el-col :span="24">
            <el-button type="primary" @click="onRemoveItem">删除</el-button>
          </el-col></el-row>
        </div>
      </el-main>
      <el-footer style="text-align: right; font-size: 10px">@版权归LINKE实验室所有</el-footer>
    </el-container>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        mysrc: "",
        mydata: [],  //存储了所有框的信息，每个框的信息由index与item构成，其中index为索引，item为拉框对象
        currentItem:null,  //表示目前选中的拉框
        originItem:null,  //更改前的拉框
        labelContent:"",  //初始化标签内容
        mymenu: { style: { left: 0, top: 0 } },
        value: [],
        options: [{
          value: 'human',
          label: 'human',
          children: [{
            value: 'woman',
            label: 'woman',
          },{
            value: 'man',
            label: 'man',
          }]
        },{
          value: 'animal',
          label: 'animal',
          children: [{
            value: 'dog',
            label: 'dog',
          },{
            value: 'cat',
            label: 'cat',
          }]
        }]
      };
    },
    mounted() {
      //document.addEventListener('mouseup', this.onMouseup)
    },
    beforeDestroy() {
      //document.removeEventListener('mouseup', this.onMouseup)
    },
    methods: {
      getXY(e) {
        let rect = this.$refs.myedit.getBoundingClientRect();
        return {
          x: e.clientX - rect.left,
          y: e.clientY - rect.top
        }
      },
      // input组件上传图片
      onChange(e) {
        this.mysrc = window.URL.createObjectURL(e.target.files[0]);
        e.target.value = '';
        this.mydata=[];
      },
      // 将上传事件传入input组件中,
      selectFile() {
        this.$refs.file.click();
      },
      //当鼠标落下
      onMousedown(e) {
        this.currentItem = null
        let { x, y } = this.getXY(e)
        this.newItem = { x, y, w: 0, h: 0,label:"", now: Date.now() }
        this.startPos = { x, y }
        this.mydata.push(this.newItem)
      },
      //当鼠标移动
      onMousemove(e) {
        if (!this.newItem) return;
        let { x, y } = this.getXY(e)
        this.newItem.w = Math.abs(x - this.startPos.x)
        this.newItem.h = Math.abs(y - this.startPos.y)
      },
      //当鼠标抬起
      onMouseup(e) {
        this.newItem = this.startPos = null;
        this.mydata = this.mydata.filter(_ => _.w > 10 && _.h > 10)  //过滤掉过小的标注框
      },
      //当右键选框时将currentItem置为此选框
      onContextmenu(item,e) {
        this.currentItem=item;
        this.originItem=item;
        this.labelContent=item.label;  //将当前标签内容置为currentItem的标签内容
        this.mymenu = {
          style: {
            top: e.clientY + 'px',
            left: e.clientX + 'px'
          }
        }
      },
      onRemoveItem() {
        this.mydata.splice(this.mydata.indexOf(this.originItem), 1); //indexOf找到该item的位置，1表示删除该元素
        this.currentItem=this.originItem=null;
      },
      getSpanStyle(item) {
        return {
          width: `${item.w}px`,
          height: `${item.h}px`,
          top: `${item.y}px`,
          left: `${item.x}px`
        };
      },
      handleChange(value) {
        //this.currentItem.label=value.toString();
        this.labelContent=value.toString();
      },
      addLabel() {
        this.currentItem.label=this.labelContent;
        this.mydata.splice(this.mydata.indexOf(this.originItem), 1,this.currentItem);
        this.currentItem=this.originItem=null;
        console.log("changeLabel");
        console.log(this.mydata);
      }
    }
  };
</script>
<style lang="less" scoped>
  .el-header {
    background-color: #B3C0D1;
    color: #333;
    line-height: 60px;
  }
  .el-footer {
    background-color: #B3C0D1;
    color: #333;
    line-height: 60px;
  }
  // 设置绘图样式1
  body {
    user-select: none;
  }

  .myMenu {
    position: fixed;
    top: 400px;
    left: 400px;
    width: 200px;
    padding: 8px 0;
    background-color: #fff;
    > * {
      width:100%
    }
  }
  .asideButton{
    margin-top: 50%;
    display: block;
  }

  // 设置绘图样式2
  .mycanvas-container {
    display: flex;
    justify-content: center;
    align-items: center;
    .left,
    .center,
    .right {
      width: 300px;
      margin-left:40%;
      p {
        text-align: center;
        font-size: 20px;
        margin-left:50%;
      }
      .myshow,
      .myedit {
        width: 450px;
        // height: 500px;
        //border: 1px solid #000;
        position: relative;
        .myedit-span {
          position: absolute;
          border: 3px dashed #fff;
          //background: url("../assets/jia.png") no-repeat center center;
          background-size: contain;
        }
        img {
          width: 100%;
        }
      }
    }
    .right {
      width: 150px;
      display: flex;
      justify-content: center;
      align-items:center;
      flex-direction: column;
    }
  }
</style>
