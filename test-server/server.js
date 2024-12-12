const express = require("express");
const app = express();
const port = 3000;

// 提供静态文件服务
app.use(express.static("public"));

// API 示例
app.get("/api/test", (req, res) => {
  res.json({ message: "PDF 测试服务正常运行！" });
});

app.get("/api/bookList",(req,res)=>{
  res.json({
    code:0,
    data:[
      {id:1,name:'JavaScript高级程序设计',author:'Nicholas C. Zakas'},
      {id:2,name:'JavaScript权威指南',author:'David Flanagan'},
      {id:3,name:'你不知道的JavaScript',author:'Kyle Simpson'},
      {id:4,name:'JavaScript设计模式',author:'Addy Osmani'},
      {id:5,name:'JavaScript语言精粹',author:'Douglas Crockford'},
    ]
  })
});

app.listen(port, () => {
  console.log(`服务运行在 http://localhost:${port}`);
});
