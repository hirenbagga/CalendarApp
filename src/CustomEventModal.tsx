// import React, { useState } from 'react';
// import TextField from '@mui/material/TextField';
// import Button from '@mui/material/Button';
// import Checkbox from '@mui/material/Checkbox';
// import FormControlLabel from '@mui/material/FormControlLabel';
// import Select from '@mui/material/Select';
// import MenuItem from '@mui/material/MenuItem';
// // import InputLabel from '@mui/material/InputLabel';
// import FormControl from '@mui/material/FormControl';
// import Dialog from '@mui/material/Dialog';
// import DialogActions from '@mui/material/DialogActions';
// import DialogContent from '@mui/material/DialogContent';
// import DialogTitle from '@mui/material/DialogTitle';
// import Grid from '@mui/material/Grid';
// import IconButton from '@mui/material/IconButton';
// import CloseIcon from '@mui/icons-material/Close';
// import { styled } from '@mui/material/styles';
// // 需要先安装date-fns依赖包
// // 运行: npm install date-fns --save
// import { format, parse, addHours } from 'date-fns';
// import AccessTimeIcon from '@mui/icons-material/AccessTime';
// import CalendarTodayIcon from '@mui/icons-material/CalendarToday';
// import PeopleIcon from '@mui/icons-material/People';
// import RepeatIcon from '@mui/icons-material/Repeat';
// import Typography from '@mui/material/Typography';

// // Define interfaces for props and events
// interface EventData {
//   id: string;
//   title: string;
//   start: string;
//   end: string;
//   isFullDay?: boolean;
//   frequency?: string;
//   until?: string;
//   people?: string[];
//   calendar?: string;
//   color?: string;
// }

// interface CustomEventModalProps {
//   isOpen: boolean;
//   onClose: () => void;
//   onSave: (event: EventData) => void;
//   initialEvent: EventData | null;
//   selectedDate: string | null;
// }

// // Styling for modal elements
// const StyledDialog = styled(Dialog)(() => ({
//   '& .MuiDialog-paper': {
//     width: '600px',
//     borderRadius: '8px'
//   }
// }));
// // theme
// const StyledButton = styled(Button)(() => ({
//   textTransform: 'none',
//   borderRadius: '20px',
//   minWidth: '80px',
//   padding: '6px 16px'
// }));

// const SaveButton = styled(StyledButton)(() => ({
//   backgroundColor: '#6a5acd',
//   color: 'white',
//   '&:hover': {
//     backgroundColor: '#5a4abf',
//   }
// }));

// // Main custom event modal component
// const CustomEventModal: React.FC<CustomEventModalProps> = ({ 
//   isOpen, 
//   onClose, 
//   onSave, 
//   initialEvent, 
//   selectedDate 
// }) => {
//   // Default event start/end is current time + 1 hour if no initialEvent
//   const defaultStart = selectedDate ? 
//     selectedDate : 
//     format(new Date(), 'yyyy-MM-dd HH:mm');
  
//   const defaultEnd = selectedDate ? 
//     format(addHours(parse(selectedDate, 'yyyy-MM-dd HH:mm', new Date()), 1), 'yyyy-MM-dd HH:mm') : 
//     format(addHours(new Date(), 1), 'yyyy-MM-dd HH:mm');
  
//   // State for form fields
//   const [event, setEvent] = useState<EventData>({
//     id: initialEvent?.id || Math.random().toString(36).substr(2, 9),
//     title: initialEvent?.title || '',
//     start: initialEvent?.start || defaultStart,
//     end: initialEvent?.end || defaultEnd,
//     isFullDay: initialEvent?.isFullDay || false,
//     frequency: initialEvent?.frequency || 'Once',
//     until: initialEvent?.until || defaultStart.split(' ')[0],
//     people: initialEvent?.people || [],
//     calendar: initialEvent?.calendar || 'Default',
//     color: initialEvent?.color || '#6a5acd'
//   });

//   // Parse dates for display
//   const startDate = event.start.split(' ')[0];
//   const startTime = event.start.split(' ')[1];
//   const endDate = event.end.split(' ')[0];
//   const endTime = event.end.split(' ')[1];

//   // Update form fields
//   const handleChange = (field: keyof EventData) => (e: React.ChangeEvent<HTMLInputElement | { name?: string; value: unknown }>) => {
//     setEvent({ ...event, [field]: e.target.value });
//   };

//   const handleCheckboxChange = (field: keyof EventData) => (e: React.ChangeEvent<HTMLInputElement>) => {
//     setEvent({ ...event, [field]: e.target.checked });
//   };

//   const handleDateChange = (field: 'startDate' | 'endDate') => (e: React.ChangeEvent<HTMLInputElement>) => {
//     const newDate = e.target.value;
//     if (field === 'startDate') {
//       const newStart = `${newDate} ${startTime}`;
//       setEvent({ 
//         ...event, 
//         start: newStart,
//         // Also update end date if it was the same as start date
//         end: endDate === startDate ? `${newDate} ${endTime}` : event.end
//       });
//     } else if (field === 'endDate') {
//       setEvent({ ...event, end: `${newDate} ${endTime}` });
//     }
//   };

//   const handleTimeChange = (field: 'startTime' | 'endTime') => (e: React.ChangeEvent<HTMLInputElement>) => {
//     const newTime = e.target.value;
//     if (field === 'startTime') {
//       setEvent({ ...event, start: `${startDate} ${newTime}` });
//     } else if (field === 'endTime') {
//       setEvent({ ...event, end: `${endDate} ${newTime}` });
//     }
//   };

//   const handleSave = () => {
//     // Format the event data for Schedule-X
//     const formattedEvent: EventData = {
//       id: event.id,
//       title: event.title,
//       start: event.isFullDay ? `${startDate} 00:00` : event.start,
//       end: event.isFullDay ? `${endDate} 23:59` : event.end,
//       isFullDay: event.isFullDay,
//       frequency: event.frequency,
//       until: event.until,
//       people: event.people,
//       calendar: event.calendar,
//       color: event.color
//     };
    
//     onSave(formattedEvent);
//     onClose();
//   };

//   // Frequency options
//   const frequencyOptions = [
//     'Once',
//     'Daily',
//     'Weekly',
//     'Monthly',
//     'Yearly'
//   ];

//   return (
//     <StyledDialog open={isOpen} onClose={onClose} maxWidth="md">
//       <DialogTitle sx={{ px: 3, py: 2, position: 'relative' }}>
//         <IconButton
//           aria-label="close"
//           onClick={onClose}
//           sx={{ position: 'absolute', right: 8, top: 8 }}
//         >
//           <CloseIcon />
//         </IconButton>
//       </DialogTitle>
      
//       <DialogContent sx={{ px: 3 }}>
//         <Grid container spacing={2}>
//           {/* Title */}
//           <Grid item xs={12} sx={{ mb: 1 }}>
//             <Typography variant="caption" sx={{ color: '#666', display: 'block', mb: 0.5 }}>
//               Title
//             </Typography>
//             <TextField
//               fullWidth
//               value={event.title}
//               onChange={handleChange('title')}
//               variant="outlined"
//               placeholder="Title"
//               size="small"
//             />
//           </Grid>
          
//           {/* Start date and time */}
//           <Grid item xs={12} sx={{ mb: 1 }}>
//             <Typography variant="caption" sx={{ color: '#666', display: 'block', mb: 0.5 }}>
//               Start date
//             </Typography>
//             <Grid container spacing={2}>
//               <Grid item xs={6}>
//                 <TextField
//                   fullWidth
//                   type="date"
//                   value={startDate}
//                   onChange={handleDateChange('startDate')}
//                   variant="outlined"
//                   size="small"
//                   InputProps={{
//                     startAdornment: (
//                       <CalendarTodayIcon fontSize="small" sx={{ mr: 1, color: '#888' }} />
//                     ),
//                   }}
//                 />
//               </Grid>
//               <Grid item xs={4}>
//                 <TextField
//                   fullWidth
//                   type="time"
//                   value={startTime}
//                   onChange={handleTimeChange('startTime')}
//                   variant="outlined"
//                   size="small"
//                   disabled={event.isFullDay}
//                   InputProps={{
//                     startAdornment: (
//                       <AccessTimeIcon fontSize="small" sx={{ mr: 1, color: '#888' }} />
//                     ),
//                   }}
//                 />
//               </Grid>
              
//               <Grid item xs={2} sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
//                 <Typography variant="body2" sx={{ color: '#666' }}>
//                   until
//                 </Typography>
//               </Grid>
//             </Grid>
//           </Grid>
          
//           {/* End date and time */}
//           <Grid item xs={12} sx={{ mb: 1 }}>
//             <Typography variant="caption" sx={{ color: '#666', display: 'block', mb: 0.5 }}>
//               End date
//             </Typography>
//             <Grid container spacing={2}>
//               <Grid item xs={6}>
//                 <TextField
//                   fullWidth
//                   type="date"
//                   value={endDate}
//                   onChange={handleDateChange('endDate')}
//                   variant="outlined"
//                   size="small"
//                   InputProps={{
//                     startAdornment: (
//                       <CalendarTodayIcon fontSize="small" sx={{ mr: 1, color: '#888' }} />
//                     ),
//                   }}
//                 />
//               </Grid>
//               <Grid item xs={4}>
//                 <TextField
//                   fullWidth
//                   type="time"
//                   value={endTime}
//                   onChange={handleTimeChange('endTime')}
//                   variant="outlined"
//                   size="small"
//                   disabled={event.isFullDay}
//                   InputProps={{
//                     startAdornment: (
//                       <AccessTimeIcon fontSize="small" sx={{ mr: 1, color: '#888' }} />
//                     ),
//                   }}
//                 />
//               </Grid>
//             </Grid>
//           </Grid>
          
//           {/* Full-day event checkbox */}
//           <Grid item xs={12} sx={{ mb: 1 }}>
//             <FormControlLabel
//               control={
//                 <Checkbox 
//                   checked={event.isFullDay} 
//                   onChange={handleCheckboxChange('isFullDay')}
//                 />
//               }
//               label="Full-day event"
//             />
//           </Grid>
          
//           {/* Frequency */}
//           <Grid item xs={6} sx={{ mb: 1 }}>
//             <Typography variant="caption" sx={{ color: '#666', display: 'block', mb: 0.5 }}>
//               Frequency
//             </Typography>
//             <FormControl fullWidth size="small">
//               <Select
//                 value={event.frequency}
//                 onChange={handleChange('frequency') as any}
//                 displayEmpty
//                 variant="outlined"
//                 startAdornment={<RepeatIcon fontSize="small" sx={{ mr: 1, color: '#888' }} />}
//               >
//                 {frequencyOptions.map(option => (
//                   <MenuItem key={option} value={option}>{option}</MenuItem>
//                 ))}
//               </Select>
//             </FormControl>
//           </Grid>
          
//           {/* Until (for recurring events) */}
//           <Grid item xs={6} sx={{ mb: 1 }}>
//             <Typography variant="caption" sx={{ color: '#666', display: 'block', mb: 0.5 }}>
//               Until
//             </Typography>
//             <TextField
//               fullWidth
//               type="date"
//               value={event.until}
//               onChange={handleChange('until')}
//               variant="outlined"
//               size="small"
//               disabled={event.frequency === 'Once'}
//               InputProps={{
//                 startAdornment: (
//                   <CalendarTodayIcon fontSize="small" sx={{ mr: 1, color: '#888' }} />
//                 ),
//               }}
//             />
//           </Grid>
          
//           {/* People */}
//           <Grid item xs={12} sx={{ mb: 1 }}>
//             <Typography variant="caption" sx={{ color: '#666', display: 'block', mb: 0.5 }}>
//               People
//             </Typography>
//             <TextField
//               fullWidth
//               placeholder="Search..."
//               variant="outlined"
//               size="small"
//               InputProps={{
//                 startAdornment: (
//                   <PeopleIcon fontSize="small" sx={{ mr: 1, color: '#888' }} />
//                 ),
//               }}
//             />
//           </Grid>
          
//           {/* Calendar */}
//           <Grid item xs={12} sx={{ mb: 2 }}>
//             <Typography variant="caption" sx={{ color: '#666', display: 'block', mb: 0.5 }}>
//               Calendar
//             </Typography>
//             <TextField
//               fullWidth
//               placeholder="Search..."
//               variant="outlined"
//               size="small"
//               InputProps={{
//                 startAdornment: (
//                   <CalendarTodayIcon fontSize="small" sx={{ mr: 1, color: '#888' }} />
//                 ),
//               }}
//             />
//           </Grid>
//         </Grid>
//       </DialogContent>
      
//       <DialogActions sx={{ px: 3, py: 2, justifyContent: 'flex-end' }}>
//         <StyledButton variant="outlined" onClick={onClose}>
//           Cancel
//         </StyledButton>
//         <SaveButton variant="contained" onClick={handleSave}>
//           Save
//         </SaveButton>
//       </DialogActions>
//     </StyledDialog>
//   );
// };

// // Define interfaces for Schedule-X
// interface CalendarInstance {
//   events: {
//     add: (event: EventData) => void;
//     update: (event: EventData) => void;
//     get: (id: string) => EventData | undefined;
//   };
//   sidebar: {
//     openAddEvent: (date?: Date) => void;
//   };
//   on: (eventName: string, callback: (arg: any) => void) => void;
// }

// interface ScheduleXPlugin {
//   name: string;
//   initialize: (calendarInstance: CalendarInstance) => void;
//   destroy: () => void;
// }

// // Custom plugin creator function
// export const createCustomEventModalPlugin = (): ScheduleXPlugin => {
//   let calendar: CalendarInstance;
//   let render: () => void;

//   return {
//     name: 'custom-event-modal',
    
//     initialize: (calendarInstance: CalendarInstance) => {
//       calendar = calendarInstance;
      
//       // Create DOM element for modal
//       const modalContainer = document.createElement('div');
//       modalContainer.id = 'sx-custom-event-modal';
//       document.body.appendChild(modalContainer);
      
//       // Initial render state
//       let isModalOpen = false;
//       let currentEvent: EventData | null = null;
//       let selectedDate: string | null = null;
      
//       // Function to render modal
//       render = () => {
//         const reactRoot = document.getElementById('sx-custom-event-modal');
        
//         if (reactRoot) {
//           import('react-dom').then(({ createRoot }) => {
//             const root = createRoot(reactRoot);
            
//             root.render(
//               <CustomEventModal
//                 isOpen={isModalOpen}
//                 onClose={() => {
//                   isModalOpen = false;
//                   render();
//                 }}
//                 onSave={(eventData) => {
//                   if (currentEvent) {
//                     // Update existing event
//                     calendar.events.update(eventData);
//                   } else {
//                     // Add new event
//                     calendar.events.add(eventData);
//                   }
//                 }}
//                 initialEvent={currentEvent}
//                 selectedDate={selectedDate}
//               />
//             );
//           });
//         }
//       };
      
//       // Override the calendar's openAddEvent method
//       const originalOpenAddEvent = calendar.sidebar.openAddEvent;
//       calendar.sidebar.openAddEvent = (date?: Date) => {
//         isModalOpen = true;
//         currentEvent = null;
//         selectedDate = date ? format(date, 'yyyy-MM-dd HH:mm') : null;
//         render();
//       };
      
//       // Listen for event clicks to edit
//       calendar.on('clickEvent', (eventId: string) => {
//         const event = calendar.events.get(eventId);
//         if (event) {
//           isModalOpen = true;
//           currentEvent = event;
//           render();
//         }
//       });
      
//       // Listen for cell clicks to add new event
//       calendar.on('clickDateTime', (date: Date) => {
//         isModalOpen = true;
//         currentEvent = null;
//         selectedDate = format(date, 'yyyy-MM-dd HH:mm');
//         render();
//       });
      
//       // Initial render
//       render();
//     },
    
//     destroy: () => {
//       const modalContainer = document.getElementById('sx-custom-event-modal');
//       if (modalContainer) {
//         document.body.removeChild(modalContainer);
//       }
//     }
//   };
// };