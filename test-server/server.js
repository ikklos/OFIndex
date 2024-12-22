const express = require('express');
const Mock = require('mockjs');

const app = express();
const port = 8080;

// 解析 JSON 请求体
app.use(express.json());

// 定义书籍数据模板
const bookTemplate = {
    'name|1': ['LearnJVM', 'Effective Java', 'Clean Code', 'Design Patterns', 'Refactoring'],
    'author|1': ['Unknown', 'Joshua Bloch', 'Robert C. Martin', 'Erich Gamma', 'Martin Fowler'],
    'description|1-50': 'Trash',
    'cover|1': ['http://via.placeholder/150'],
    'tag|1': ['CS', 'Programming', 'Software Engineering', 'Web Development', 'Database']
};

function generateBooks(count) {
    return Mock.mock({
        [`books|${count}`]: [bookTemplate]
    }).books;
}


// 模拟搜索接口
app.post('/search', (req, res) => {
    const { text, bookClass, count, page } = req.body;
    //print body
    console.log(req.body);
    // 验证请求参数
    

    if (typeof bookClass !== 'number' || bookClass < 0) {
        console.log("inv bookclass")
        return res.status(400).json({
            result: false,
            message: "Invalid 'bookClass' parameter",
            count: 0,
            totalResult: 0,
            items: []
        });
    }

    if (typeof count !== 'number' || count < 0) {
        console.log("inv count")
        return res.status(400).json({
            result: false,
            message: "Invalid 'count' parameter",
            count: 0,
            totalResult: 0,
            items: []
        });
    }

    if (typeof page !== 'number' || page < 0) {
        console.log("inv page")
        return res.status(400).json({
            result: false,
            message: "Invalid 'page' parameter",
            count: 0,
            totalResult: 0,
            items: []
        });
    }

    // 模拟总结果数
    const totalResult = 100; // 假设总结果数为 100 或更多

    // 计算当前页的起始索引
    const startIndex = (page - 1) * count;
    const endIndex = startIndex + count;

    // 生成随机书籍数据
    const books = generateBooks(count);

    // 返回响应
    res.json({
        result: true,
        message: "Result found",
        count: books.length,
        totalResult,
        items: books
    });
});
// 定义硬编码的图书类别
const bookClasses = [
    {
        id:0,
        name:'全部'
    },
    {
        id: 1,
        name: "小说"
    },
    {
        id: 2,
        name: "科幻"
    },
    {
        id: 3,
        name: "编程"
    },
    {
        id: 4,
        name: "历史"
    },
    {
        id: 5,
        name: "哲学"
    }
];
// 模拟 /class 接口
app.get('/class', (req, res) => {
    // 返回硬编码的图书类别
    res.json({
        result: true,
        message: "BookClass Found",
        count: bookClasses.length,
        items: bookClasses
    });
});

// 启动服务器
app.listen(port, () => {
    console.log(`Mock server is running on http://localhost:${port}`);
});