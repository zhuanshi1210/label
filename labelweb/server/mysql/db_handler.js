var DbHandler={
    getImgPath:'select path from original_pic where pid=?',
    getPostPath:'select postpath from postpath where pid=?'
};
module.exports=DbHandler;