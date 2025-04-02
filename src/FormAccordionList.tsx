import React, { useState } from 'react';
import {
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Typography,
  TextField,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Box,
  SelectChangeEvent,
  Stack,
  Button
} from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';

interface FormData {
  id: string;
  title: string;
  time: string;
  description: string;
  frequency: string;
}

const frequencyOptions = [
  { value: 'daily', label: 'Daily' },
  { value: 'weekly', label: 'Weekly' },
  { value: 'monthly', label: 'Monthly' },
  { value: 'yearly', label: 'Yearly' }
];

interface AccordionItemProps {
  data: FormData;
  onChange: (id: string, updatedData: Partial<FormData>) => void;
  expanded: string | false;
  onExpand: (panel: string) => void;
}

const AccordionItem: React.FC<AccordionItemProps> = ({ data, onChange, expanded, onExpand }) => {
  const [isEditing, setIsEditing] = useState(false);

  const handleChange = (field: keyof FormData) => (
    event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement> | SelectChangeEvent
  ) => {
    onChange(data.id, { [field]: event.target.value });
  };

  return (
    <Accordion expanded={expanded === data.id} onChange={() => onExpand(data.id)}>
      <AccordionSummary expandIcon={<ExpandMoreIcon />} color="secondary">
        <Typography variant="subtitle1" fontWeight={expanded === data.id ? 'bold' : 'normal'}>
          {data.title || 'New Item'}
        </Typography>
      </AccordionSummary>
      <AccordionDetails>
        <Box component="form" noValidate color="secondary">
          <Stack spacing={2}>
            <TextField
              fullWidth
              color="secondary"
              label="Title"
              value={data.title}
              onChange={handleChange('title')}
              variant="outlined"
              disabled={!isEditing}
            />
            <TextField
              fullWidth
              color="secondary"
              label="Time"
              type="datetime-local"
              value={data.time || ""}
              onChange={handleChange("time")}
              variant="outlined"
              InputLabelProps={{ shrink: true }}
              disabled={!isEditing}
              sx={{
                "& input": {
                  accentColor: "#8a2be2", // 这里设置为紫色（你可以换成别的颜色）
                },
              }}
            />


            <TextField
              color="secondary"
              fullWidth
              label="Description"
              value={data.description}
              onChange={handleChange('description')}
              multiline
              rows={3}
              variant="outlined"
              disabled={!isEditing}
            />
            <FormControl fullWidth variant="outlined" disabled={!isEditing} color="secondary">
              <InputLabel id={`frequency-label-${data.id}`}>Frequency</InputLabel>
              <Select
                labelId={`frequency-label-${data.id}`}
                value={data.frequency}
                onChange={handleChange('frequency')}
                label="Frequency"
                color="secondary"
              >
                {frequencyOptions.map((option) => (
                  <MenuItem key={option.value} value={option.value}>
                    {option.label}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
            <Stack direction="row" spacing={2}>
              <Button
                variant="contained"
                color="secondary"
                onClick={() => setIsEditing(true)}
                disabled={isEditing}
              >
                Change
              </Button>
              <Button
                variant="contained"
                color="secondary"
                onClick={() => setIsEditing(false)}
                disabled={!isEditing}
              >
                Save
              </Button>
            </Stack>
          </Stack>
        </Box>
      </AccordionDetails>
    </Accordion>
  );
};

interface FormAccordionListProps {
  items: FormData[];
  onChange: (updatedItems: FormData[]) => void;
}

const FormAccordionList: React.FC<FormAccordionListProps> = ({ items, onChange }) => {
  const [expanded, setExpanded] = useState<string | false>(items.length > 0 ? items[0].id : false);

  const handleExpand = (panel: string) => {
    setExpanded(expanded === panel ? false : panel);
  };

  const handleItemChange = (id: string, updatedData: Partial<FormData>) => {
    const updatedItems = items.map(item =>
      item.id === id ? { ...item, ...updatedData } : item
    );
    onChange(updatedItems);
  };

  return (
    <Box sx={{ width: '100%' }}>
      {items.map((item) => (
        <AccordionItem
          key={item.id}
          data={item}
          onChange={handleItemChange}
          expanded={expanded}
          onExpand={handleExpand}
        />
      ))}
    </Box>
  );
};

export default FormAccordionList;