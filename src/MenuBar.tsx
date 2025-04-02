import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import AccountCircle from '@mui/icons-material/AccountCircle';
import { useNavigate } from 'react-router-dom';  
import MenuItem from '@mui/material/MenuItem';
import Menu from '@mui/material/Menu';

export default function MenuAppBar() {
  const [auth, setAuth] = React.useState(false);  // 默认为未登录
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
  const navigate = useNavigate();  // 使用 useNavigate 钩子

  // const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
  //   setAuth(event.target.checked);  // 切换登录状态
  // };

  const handleMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);  // 打开菜单
  };

  const handleClose = () => {
    setAnchorEl(null);  // 关闭菜单
  };

  const handleLogout = () => {
    setAuth(false);  // 退出登录
    handleClose();  // 关闭菜单
  };

  const handleLogin = () => {

    setAuth(true);  // 登录
    navigate('/login');
    handleClose();  // 关闭菜单
  };

  return (
    <Box sx={{ flexGrow: 1 }}>
        {/* color="secondary" */}
      <AppBar position="static"  color="default">
      {/* <AppBar position="static" sx={{ backgroundColor: 'transparent' }}> 设置 AppBar 背景色 */}

        <Toolbar>
          <IconButton
            size="large"
            edge="start"
            color="inherit"
            aria-label="menu"
            sx={{ mr: 2 }}
          >
            <MenuIcon />
          </IconButton>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
           
          </Typography>
          <div>
            <IconButton
              size="large"
              aria-label="account of current user"
              aria-controls="menu-appbar"
              aria-haspopup="true"
              onClick={handleMenu}
              color="inherit"
            >
              <AccountCircle />
            </IconButton>
            
            <Menu
              id="menu-appbar"
              anchorEl={anchorEl}
              anchorOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
              keepMounted
              transformOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
              open={Boolean(anchorEl)}
              onClose={handleClose}
            >
              {auth ? (
                <MenuItem onClick={handleLogout}>Logout</MenuItem>  // 显示 Logout
              ) : (
                <MenuItem onClick={handleLogin}>Login In</MenuItem>  // 显示 Login In
              )}
            </Menu>
          </div>
        </Toolbar>
      </AppBar>
    </Box>
  );
}
