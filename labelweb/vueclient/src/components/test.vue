<template>
  <div id="label">
    <button @click="onUpload">上传图片</button> <!--顶部上传按钮-->
    <button @click="onPickFile">选择图片</button>  <!--浏览图片按钮-->
    <canvas id="myCanvas" width="200" height="100"></canvas>
    <input
      type="file"
      ref="fileInput"
      accept="image/*"
      @change="getFile"
      style="display:none"
    >
    <img :src= "imageUrl"  alt="logo"/>  <!--显示图片-->
  </div>
</template>

<script>
    export default {
        name: "test",
      data(){
          return{
            imageUrl:"",  //当前显示图片的src地址
            postUrl:""    //图片上传地址,暂时用本地文件夹做测试，地址为file:///D:/upload
          }
      },
      mounted(){
          //绘制矩形
        var c=document.getElementById("myCanvas");
        var cxt=c.getContext("2d");
        cxt.fillStyle="#FF0000";
        cxt.fillRect(0,0,150,75);
          //请求后台，获得图片上传地址
        this.$reqs.get('/label/getPostPath',{params:{id:'1'}}).then(res=>{
          if(res.status===200&&res.data){
            this.postUrl=res.data.data
            console.log(this.postUrl);
          }
        })
      },
      methods:{
          onPickFile(){
            this.$refs.fileInput.click()
          },
        getFile(event){
            const files=event.target.files
            let filename=files[0].name
            if(filename.lastIndexOf('.')<=0){
              return alert("Please add a valid image!")
            }
            const fileReader=new FileReader()
          fileReader.addEventListener('load',()=>{
            this.imageUrl=fileReader.result
          })
          // 将图片将转成 base64 格式
          fileReader.readAsDataURL(files[0])
          this.image=files[0]
        },
        onUpload(event){
            event.preventDefault();
            let fd=new FormData()
            fd.append('file',this.image)
            fd.append('postpath',this.postUrl)
            //console.log(fd.get('file'));
            this.$reqs.post('/label/upload',fd,{headers:{"Content-Type":"multipart/form-data"}}).then(res=>{
              /*if(res.status===200&&res.data){
                let data=res.data.data
                this.imageUrl=data.url
                console.log("imageUrl: "+this.imageUrl)
              }*/

              rsp.send('success');
            }).catch(error=>{
              console.log(error)
            })
        }
      }
    }
</script>

<style scoped>

</style>
