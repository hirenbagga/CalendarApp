// // Calendar.tsx
// import { useEffect, useState } from 'react';
// import { ScheduleXCalendar, useCalendarApp } from "@schedule-x/react";
// import { createViewWeek, createViewMonthGrid } from '@schedule-x/calendar';
// import '@schedule-x/theme-default/dist/calendar.css';
// import { createEventModalPlugin } from '@schedule-x/event-modal';
// import { createDragAndDropPlugin } from '@schedule-x/drag-and-drop';
// import { createEventsServicePlugin } from '@schedule-x/events-service';
// import Container from '@mui/material/Container';
// import Button from '@mui/material/Button';
// import EventDialog from './EventDialog';
// import { useNavigate } from 'react-router-dom';



// const Calendar: React.FC = () => {
//     // const [value, setValue] = useState(0);
//     const [value] = useState(0);
//     const [showDialog, setShowDialog] = useState(false);
//     const eventsService = useState(() => createEventsServicePlugin())[0];
//     const navigate = useNavigate();  // 获取导航方法
//     const handleSave = (eventData: any) => {
//         console.log('Saved event:', eventData);
//         setShowDialog(false);
//     };

//     const calendar = useCalendarApp({
//         views: [
//             createViewWeek(),
//             createViewMonthGrid()
//         ],
//         events: [
//             {
//                 id: 1,
//                 title: 'My new event',
//                 start: '2025-01-01 00:00',
//                 end: '2025-01-01 02:00',
//                 description: 'zoom.us/123456',
//                 color: '#FF0000'
//             }
//         ],
//         selectedDate: '2025-01-01',
//         plugins: [
//             createEventModalPlugin(),
//             createDragAndDropPlugin(),
//             eventsService
//         ]
//     });

//     useEffect(() => {
//         eventsService.getAll();
//     }, [eventsService]);

//     return (
//         <>
//             < Container maxWidth="lg" >
//                 <div style={{ position: 'relative' }}>


//                     <Button
//                         variant="contained"
//                         color="primary"
//                         onClick={() => setShowDialog(true)}
//                         style={{
//                             backgroundColor: "#F1F3F4",
//                             color: "#000",
//                             fontWeight: "bold",
//                             boxShadow: "none",
//                             marginRight: "10px"
//                         }}
//                     >
//                         ADD
//                     </Button>

//                     {showDialog && (
//                         <div style={{
//                             position: 'absolute',
//                             zIndex: 1000,
//                             top: '20px',
//                         }}>
//                             <EventDialog
//                                 onSave={handleSave}
//                                 onCancel={() => setShowDialog(false)}
//                             />
//                         </div>
//                     )}

//                     {calendar && <ScheduleXCalendar calendarApp={calendar} />}
//                 </div>
//             </Container >
//             {/* <BottomNavigation
//                 showLabels
//                 value={value}
//                 // onChange={(event, newValue) => setValue(newValue)}
//                 // onChange={(event, newValue) => setValue(newValue)}
//                 sx={{ position: 'fixed', bottom: 0, width: '100%' }}
//             >
//                 <BottomNavigationAction label="Recents" icon={<RestoreIcon />} />
//                 <BottomNavigationAction label="Favorites" icon={<FavoriteIcon />} />
//                 <BottomNavigationAction label="Nearby" icon={<LocationOnIcon />} />
//             </BottomNavigation> */}
//         </>
//     );
// };

// export default Calendar;




