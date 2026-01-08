<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>密码重置确认</title>
    <style>
        /* 保留：基础样式（必要的全局重置）*/
        * { margin: 0; padding: 0; box-sizing: border-box; }

        body {
            font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
            background-color: #f5f7fa;
            color: #333333;
            line-height: 1.6;
            padding: 20px 0;
        }

        /* 容器与头部 */
        .email-container {
            max-width: 600px;
            margin: 0 auto;
            background-color: #ffffff;
            border-radius: 12px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
            overflow: hidden;
        }

        .header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding: 40px 20px;
            text-align: center;
            color: white;
        }

        .header h1 {
            font-size: 28px;
            font-weight: 600;
            margin-top: -10px;
            margin-bottom: -10px;
            letter-spacing: 1px;
        }

        /* 内容区 */
        .content {
            padding: 40px 30px 10px 30px;
        }

        .intro-text {
            font-size: 18px;
            margin-top: -10px;
            line-height: 175%;
            margin-bottom: 10px;
            color: #555;
            text-align: left;
        }

        /* 密码展示区 */
        .password-section {
            background-color: #f8f9fa;
            border-radius: 8px;
            padding: 25px;
            margin: 30px 0;
            text-align: center;
            border-left: 4px solid #667eea;
        }

        .password-label {
            font-size: 14px;
            color: #6c757d;
            margin-bottom: 10px;
            display: block;
        }

        .password {
            font-size: 32px;
            font-weight: 700;
            color: #e74c3c;
            letter-spacing: 3px;
            font-family: 'Courier New', monospace;
            padding: 15px;
            background-color: #fff;
            border-radius: 6px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
            margin: 10px 0;
            display: inline-block;
            min-width: 250px;
        }

        /* 底部 */
        .footer {
            background-color: #f8f9fa;
            padding: 25px;
            text-align: center;
            font-size: 14px;
            color: #6c757d;
            border-top: 1px solid #e9ecef;
        }

        .footer p {
            color: #e74c3c;
            margin-bottom: 10px;
        }

        /* 响应式（仅保留影响到页面上实际元素的规则） */
        @media (max-width: 480px) {
            .content { padding: 25px 20px; }
            .header { padding: 30px 15px; }
            .header h1 { font-size: 24px; }
            .password {
                font-size: 24px;
                min-width: 200px;
                padding: 12px;
            }
        }
    </style>
</head>
<body>
<div class="email-container">
    <div class="header">
        <h1>您的宿舍管理平台密码已重置</h1>
    </div>

    <div class="content">
        <p class="intro-text">${realName}，您好。</p>
        <p class="intro-text">
            您在宿舍管理平台的密码已重置成功，为保障您的账户安全，请尽快登录系统并完成修改。
        </p>

        <div class="password-section">
            <span class="password-label">您的新密码</span>
            <div class="password">${newPwd}</div>
        </div>
    </div>

    <div class="footer">
        <p>此邮件由系统自动发送，请不要直接回复</p>
    </div>
</div>
</body>
</html>
