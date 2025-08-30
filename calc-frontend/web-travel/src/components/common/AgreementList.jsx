import React from 'react';
import { List, Paper, Typography } from '@mui/material';

export default function AgreementList({ agreements }) {
  if (!agreements || agreements.length === 0) {
    return <Typography>No agreements found.</Typography>;
  }

  return (
    <List>
      {agreements.map((agreement) => (
        <Paper key={agreement.uuid} sx={{ mb: 2, p: 2 }}>
          <Typography variant="subtitle1" gutterBottom>
            Agreement UUID: {agreement.uuid}
          </Typography>
          <Typography>
            Date From: {agreement.agreementDateFrom && new Date(agreement.agreementDateFrom).toLocaleString()}
          </Typography>
          <Typography>
            Date To: {agreement.agreementDateTo && new Date(agreement.agreementDateTo).toLocaleString()}
          </Typography>
          <Typography>
            Countries: {agreement.countriesToVisit && agreement.countriesToVisit.join(', ')}
          </Typography>
          <Typography>
            Risks: {agreement.selectedRisks && agreement.selectedRisks.join(', ')}
          </Typography>
        </Paper>
      ))}
    </List>
  );
}
