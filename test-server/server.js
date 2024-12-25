const express = require('express');
const fs = require('fs');
const path = require('path');
const Mock = require('mockjs');
const { RateLimiter } = require('limiter');
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
    'tag|1': [['CS', 'Programming', 'Software Engineering', 'Web Development', 'Database']]
};

function generateBooks(count) {
    return Mock.mock({
        [`books|${count}`]: [bookTemplate]
    }).books;
}

app.post('/login', (req, res) => {
    const { username, password } = req.body;
    console.log(req.body);
        return res.json({
            result: true,
            message: "Login success",
            token: "1234567890"
        });
});


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
        id: 0,
        name: '全部'
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

// /book/{id} 接口 返回书籍详情
app.get('/book/:id', (req, res) => {
    const { id } = req.params;
    console.log(req.body);
    // 验证 ID 是否为数字
    if (isNaN(id)) {
        return res.status(400).json({
            result: false,
            message: "Invalid 'id' parameter"
        });
    }

    // 返回响应
    res.json({
        result: true,
        message: "Book found",
        bookId: 0,
        name: "LearnCSharp",
        author: "Unknown",
        description: "GarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbageGarbage",
        cover: "https://i0.hdslb.com/bfs/new_dyn/7a4406b34701987f39b4614f23b08e782183230.jpg",
        tag: ["tag1", "tag2"],
        isbn: "114514",
        bookClass: 0
    });
});

// /search/pack/{bookId}
app.get('/search/pack/:bookId', (req, res) => {
    const { bookId } = req.params;
    console.log(req.body);
    // 验证 ID 是否为数字
    if (isNaN(bookId)) {
        return res.status(400).json({
            result: false,
            message: "Invalid 'bookId' parameter"
        });
    }

    // 返回响应
    res.json({
        message: "null",
        count: 2,
        items: [
            {
                packId: 0,
                name: "GenshinPack",
                authorId: 0,
                authorAvatar: "?",
                description: "null"
            },
            {
                packId: 1,
                name: "WuWaPack",
                authorId: 52,
                authorAvatar: "null",
                description: "thisisadescription"
            },
            {
                packId: 1,
                name: "WuWaPack",
                authorId: 52,
                authorAvatar: "null",
                description: "thisisadescription"
            },
            {
                packId: 1,
                name: "WuWaPack",
                authorId: 52,
                authorAvatar: "null",
                description: "thisisadescription"
            },
            {
                packId: 1,
                name: "WuWaPack",
                authorId: 52,
                authorAvatar: "null",
                description: "thisisadescription"
            },
            {
                packId: 1,
                name: "WuWaPack",
                authorId: 52,
                authorAvatar: "null",
                description: "thisisadescription"
            },
            {
                packId: 1,
                name: "WuWaPack",
                authorId: 52,
                authorAvatar: "null",
                description: "thisisadescription"
            },
            {
                packId: 1,
                name: "WuWaPack",
                authorId: 52,
                authorAvatar: "null",
                description: "thisisadescription"
            },
            {
                packId: 1,
                name: "WuWaPack",
                authorId: 52,
                authorAvatar: "null",
                description: "thisisadescription"
            }
        ]

    })
});

///load/ebook/{bookid}
// 模拟的书籍文件路径（请根据实际情况修改）
const booksPath = path.join(__dirname, 'books');

// 确保书籍文件夹存在
if (!fs.existsSync(booksPath)) {
    fs.mkdirSync(booksPath);
}
// 创建限速器（例如：每秒 50 KB）
const limiter = new RateLimiter(50 * 1024, 'second'); // Limit to 50 KB/s


const { Transform } = require('stream');

// 下载电子书的路由
app.get('/load/ebook/:bookId', (req, res) => {
    const { bookId } = req.params;
    console.log(bookId);
    // 验证 ID 是否为数字
    if (isNaN(bookId)) {
        return res.status(400).json({
            result: false,
            message: "Invalid 'bookId' parameter",
        });
    }

    // 构建文件路径
    const filePath = path.join(booksPath, `test.pdf`);

    // 检查文件是否存在
    if (!fs.existsSync(filePath)) {
        return res.status(404).json({
            result: false,
            message: "Book not found",
        });
    }

    // 获取文件大小
    const stats = fs.statSync(filePath);
    const fileSize = stats.size;

    // 设置响应头
    res.setHeader('Content-Type', 'application/octet-stream');
    res.setHeader('Content-Length', fileSize);
    res.setHeader('Content-Disposition', `attachment; filename=book_${bookId}.pdf`);

    // 限速器（每秒 50 KB）
    const speedLimiter = new Transform({
        transform(chunk, encoding, callback) {
            setTimeout(() => {
                callback(null, chunk);
            }, (chunk.length / (100 * 1024)) * 1000); // 计算延迟时间
        },
    });

    // 创建读取流
    const readStream = fs.createReadStream(filePath);

    // 处理流错误
    readStream.on('error', (err) => {
        console.error("File read error:", err);
        res.status(500).json({
            result: false,
            message: "Error reading file",
        });
    });

    // 将流通过限速器传递给响应
    readStream.pipe(speedLimiter).pipe(res);

    // 处理响应完成
    res.on('finish', () => {
        console.log(`File sent: book_${bookId}.pdf`);
    });
});


// Helper function to generate a random date in the past year
function randomDate() {
    const now = new Date();
    const start = new Date(now.setFullYear(now.getFullYear() - 1));
    return new Date(start.getTime() + Math.random() * (now.getTime() - start.getTime())).toISOString();
}

// Generate sample book items
function generateBooksd(startId, count) {
    return Array.from({ length: count }, (_, i) => ({
        bookId: startId + i,
        addTime: randomDate(),
        name: `Book ${startId + i}`
    }));
}

// Generate four book lists with 4-5 books each and a default list with 4-5 books
function generateSampleData() {
    const bookLists = [];

    // Default book list
    const defaultBooksCount = Mock.Random.integer(4, 5);
    bookLists.push({
        name: "Default",
        index: 1,
        count: defaultBooksCount,
        books: generateBooksd(0, defaultBooksCount)
    });

    // Other book lists
    for (let i = 2; i <= 5; i++) {
        const booksCount = Mock.Random.integer(4, 5);
        bookLists.push({
            name: `Custom List ${i}`,
            index: i,
            count: booksCount,
            books: generateBooksd((i - 1) * 5, booksCount),
            cover: `http://via.placeholder/150?text=List${i}`
        });
    }

    return {
        message: "dsasadsda",
        count: bookLists.length, items: bookLists
    };
}

// API endpoint to return the generated data
app.get('/shelf', (req, res) => {
    res.json(generateSampleData());
});

app.get('/forum/posts', (req, res) => {
    res.json({
        message: null,
        count: 1,
        total: 1,
        posts: [
            {
                message: "sdasd",
                postId: 152,
                posterId: 52,
                posterName: "督娟",
                posterAvatar: "https://avatars.githubusercontent.com/u/61472523",
                bookId: 0,
                packId: 0,
                tags: [
                    "Mihoyo"
                ],
                title: "撑举没啃熟练甚至地址发型哼但是",
                images: ["", ""],
                likes: 0,
                createTime: "2024-12-23T16:15:02"
            },
            {
                message: "sdasd",
                postId: 152,
                posterId: 52,
                posterName: "督娟",
                posterAvatar: "https://avatars.githubusercontent.com/u/61472523",
                bookId: 0,
                packId: 0,
                tags: [
                    "Mihoyo"
                ],
                title: "撑举没啃熟练甚至地址发型哼但是",
                images: ["", ""],
                likes: 0,
                createTime: "2024-12-23T16:15:02"
            },
            {
                message: "sdasd",
                postId: 152,
                posterId: 52,
                posterName: "督娟",
                posterAvatar: "https://avatars.githubusercontent.com/u/61472523",
                bookId: 0,
                packId: 0,
                tags: [
                    "Mihoyo"
                ],
                title: "撑举没啃熟练甚至地址发型哼但是",
                images: ["", ""],
                likes: 0,
                createTime: "2024-12-23T16:15:02"
            },
        ]
    })
});


// 启动服务器
app.listen(port, () => {
    console.log(`Server is running on http://localhost:${port}`);
});

