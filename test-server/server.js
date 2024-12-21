const express = require('express');
const jwt = require('jsonwebtoken');
const app = express();
const port = 3000;

// 使用中间件解析JSON请求体
app.use(express.json());

// JWT验证中间件
function authenticateToken(req, res, next) {
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1]; // Bearer <token>

    if (token == null) return res.sendStatus(401); // 如果没有提供token

    jwt.verify(token, 'your_secret_key', (err, user) => {
        if (err) return res.sendStatus(601); // 如果token无效
        console.log('Verified token:', token); // 输出验证后的token
        req.user = user; // 将用户信息附加到req对象上
        next(); // 继续处理请求
    });
}

// 登录路由
app.post('/login', (req, res) => {
    // 假设我们有一个简单的用户验证逻辑
    const { userid, passwd } = req.body;
        const token = jwt.sign({ user: userid }, 'your_secret_key', { expiresIn: '1h' });
        // 返回带有token的JSON响应
        return res.json({ token });

    
});

app.get('/books', (req, res) => {
    // // 硬编码的书籍数据
    // const books = [
    //     { id: '1', title: 'Book 1', author: 'Author' },
    //     { id: '2', title: 'Book 2', author: 'Author' }
    // ]
    // res.json(books);
    //返回601状态码
    console.log('601');
    res.status(601).json({ message: 'Invalid token' });
});


app.post('/register', (req, res) => {
  // 假设我们有一个简单的用户验证逻辑
  const { userid, passwd } = req.body;
  return res.json({ userid: '1' });
});

// 获取书籍详情的路由，需要验证token
app.get('/book/:id', authenticateToken, (req, res) => {
    const bookId = req.params.id;

    // 硬编码ID为1的书籍数据
    if (bookId === '1') {
        const book = {
            id: '1',
            title: 'Test Book',
            author: 'Test Author'
        };
        res.json(book);
    } else {
        res.status(404).json({ message: 'Book not found' });
    }
});

// 启动服务器
app.listen(port, () => {
    console.log(`服务器正在 http://localhost:${port} 上运行`);
});