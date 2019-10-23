var express=require('express');
var assert = require('assert');
var router=express.Router();
//连接数据库
var mysql=require('mysql');
var dbConfig=require('../mysql/mysqlconfig');
var dbHandler=require('../mysql/db_handler');
var pool=mysql.createPool(dbConfig.mysql);
//接收formdata数据
var multer = require('multer');
var UUID = require('uuid');
var postpath="";
_this=this;
router.post('/upload', function(req,res,next)
{
    //设置保存规则
    var storage = multer.diskStorage({
        //destination：字段设置上传路径，可以为函数
        destination: function (req, file, cb) {
           cb(null, _this.postpath)
        },
        //filename：设置文件保存的文件名
        filename: function(req, file, cb) {
            let extName = file.originalname.slice(file.originalname.lastIndexOf('.'))
            //let fileName = UUID.v1()
            let fileName=file.fieldname + '-' + Date.now();
            cb(null, fileName + extName)
        }
    });
    //创建 multer 实例
    imageUploader = multer({
        storage: storage,
    });
    imageUploader.single('file')(req, res, function (err) {
        if (err instanceof multer.MulterError) {
            console.log("2");
            console.log(err);
        }
        else if (err) {
            // 发生错误
            console.log("3");
            console.log(err);
        }
        console.log('success');
    });
});

router.get('/getPostPath',function(req,res,next)
{
    pool.getConnection(function(err,connection)
    {
        if(err)
        {
            console.log(err)
            res.end('{"err":"数据库未连接成功"}');
        }
        else
        {
            connection.query(dbHandler.getPostPath,[req.query.id],function(err,result)
            {
                try
                {
                    assert.equal(null,err);
                }
                catch(e)
                {
                    console.log(e);
                    res.end('{"success":"false","err":"数据库查询出现错误"}');
                }
                _this.postpath=result[0].postpath;
                var obj = {
                    data:_this.postpath,
                    success:"true"
                };
                var str = JSON.stringify(obj);
                res.end(str);
            });
            connection.release();
        }
    });

});
module.exports = router;