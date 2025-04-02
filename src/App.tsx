
import React from 'react';
import { BrowserRouter as Router, Route, Routes, useNavigate, useLocation } from 'react-router-dom';
import Timer from './Timer';
import Clander from './clander';
// import TodoList from './TodoList';
import Login from './login/Login';
import Register from './login/Register';
import ForgotPassword from './login/ForgotPassword';
import BottomNavigation from '@mui/material/BottomNavigation';
import BottomNavigationAction from '@mui/material/BottomNavigationAction';
import RestoreIcon from '@mui/icons-material/Restore';
import FavoriteIcon from '@mui/icons-material/Favorite';
import LocationOnIcon from '@mui/icons-material/LocationOn';

import TodoList from './TodoList2';
// 创建一个带有底部导航的布局组件
const Layout = () => {
  const navigate = useNavigate();
  const location = useLocation();

  // 根据当前路径设置激活的导航项
  const getNavValue = () => {
    const path = location.pathname;
    if (path === '/timer') return 0;
    if (path === '/todolist') return 1;
    if (path === '/clander') return 2;
    return 0;
  };

  return (
    <div style={{ paddingBottom: '56px' }}>
      <Routes>
        <Route path="/clander" element={<Clander />} />
        <Route path="/" element={<Clander />} />
        <Route path="/timer" element={<Timer />} />
        {/* <Route path="/todolist" element={<FormAccordionList />} /> */}
        <Route path="/todolist" element={<TodoList />} />
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
        <Route path="/forgotpassword" element={<ForgotPassword />} />
        {/* <Route path="/forgotpassword" element={<ForgotPassword />} /> */}
      </Routes>

      <BottomNavigation
        showLabels
        value={getNavValue()}
        onChange={(_, newValue) => {
          switch (newValue) {
            case 0:
              navigate('/timer');
              break;
            case 1:
              navigate('/todolist');
              break;
            case 2:
              navigate('/clander');
              break;
            default:
              navigate('/');
          }
        }}
        sx={{ position: 'fixed', bottom: 0, width: '100%', zIndex: 1000, boxShadow: '0 -2px 10px rgba(0, 0, 0, 0.1)' }}
      >
        <BottomNavigationAction label="Timer" icon={<RestoreIcon />} />
        <BottomNavigationAction label="Todo List" icon={<FavoriteIcon />} />
        <BottomNavigationAction label="Calendar" icon={<LocationOnIcon />} />
      </BottomNavigation>
    </div>
  );
};

const App: React.FC = () => {
  return (
    <Router>
      <Layout />
    </Router>
  );
};

export default App;