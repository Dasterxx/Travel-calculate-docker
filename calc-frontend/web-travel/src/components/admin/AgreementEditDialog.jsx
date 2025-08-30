import React, { useState, useEffect } from 'react';
import {
  Dialog, DialogTitle, DialogContent, DialogActions,
  TextField, Button, Alert, Stack
} from '@mui/material';

export default function AgreementEditDialog({ open, agreement, onClose, onSave }) {
  const [formData, setFormData] = useState({
    agreementDateFrom: '',
    agreementDateTo: '',
    countriesToVisit: '',
    insuranceLimit: '',
    agreementPremium: '',
    selectedRisks: ''
  });
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (agreement) {
      setFormData({
        agreementDateFrom: agreement.agreementDateFrom || '',
        agreementDateTo: agreement.agreementDateTo || '',
        countriesToVisit: agreement.countriesToVisit.join(', ') || '',
        insuranceLimit: agreement.insuranceLimit || '',
        agreementPremium: agreement.agreementPremium || '',
        selectedRisks: agreement.selectedRisks.join(', ') || ''
      });
    }
  }, [agreement]);

  const handleChange = e => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async e => {
    e.preventDefault();
    setError('');
    setSaving(true);

    try {
        const padSeconds = (dtString) => {
          if (!dtString) return dtString;
          // если строка без секунд (длина 16) добавим ":00"
          return dtString.length === 16 ? dtString + ':00' : dtString;
        };

      const body = {
        agreement: {
          agreementDateFrom: padSeconds(formData.agreementDateFrom),
          agreementDateTo: padSeconds(formData.agreementDateTo),
          countriesToVisit: formData.countriesToVisit.split(',').map(c => c.trim()).filter(Boolean),
          insuranceLimit: Number(formData.insuranceLimit),
          agreementPremium: Number(formData.agreementPremium),
          selectedRisks: formData.selectedRisks.split(',').map(r => r.trim()).filter(Boolean)
        }
      };

      const resp = await fetch(`/api/admin/agreements/${agreement.uuid}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${localStorage.getItem('token')}`
        },
        body: JSON.stringify(body)
      });

      if (resp.ok) {
        const updatedAgreement = await resp.json();
        onSave(updatedAgreement.data);
      } else {
        const data = await resp.json();
        setError(data.message || 'Failed to save agreement');
      }
    } catch {
      setError('Network error');
    } finally {
      setSaving(false);
    }
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle>Edit Agreement</DialogTitle>
      <DialogContent>
        {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
        <form id="edit-agreement-form" onSubmit={handleSubmit}>
          <Stack spacing={2}>
            <TextField
              label="Date From"
              name="agreementDateFrom"
              type="datetime-local"
              value={formData.agreementDateFrom}
              onChange={handleChange}
              InputLabelProps={{ shrink: true }}
              required
            />
            <TextField
              label="Date To"
              name="agreementDateTo"
              type="datetime-local"
              value={formData.agreementDateTo}
              onChange={handleChange}
              InputLabelProps={{ shrink: true }}
              required
            />
            <TextField
              label="Countries to Visit (comma separated)"
              name="countriesToVisit"
              value={formData.countriesToVisit}
              onChange={handleChange}
            />
            <TextField
              label="Insurance Limit"
              name="insuranceLimit"
              type="number"
              value={formData.insuranceLimit}
              onChange={handleChange}
            />
            <TextField
              label="Agreement Premium"
              name="agreementPremium"
              type="number"
              value={formData.agreementPremium}
              onChange={handleChange}
            />
            <TextField
              label="Selected Risks (comma separated)"
              name="selectedRisks"
              value={formData.selectedRisks}
              onChange={handleChange}
            />
          </Stack>
        </form>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} disabled={saving}>Cancel</Button>
        <Button form="edit-agreement-form" type="submit" variant="contained" disabled={saving}>
          {saving ? 'Saving...' : 'Save'}
        </Button>
      </DialogActions>
    </Dialog>
  );
}
