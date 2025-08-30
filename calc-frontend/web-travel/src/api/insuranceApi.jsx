export const calculateInsurance = async (data) => {
  const response = await fetch('/insurance/travel/web/v3', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data)
  });

  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(errorData.message || 'Insurance calculation failed');
  }

  return response.json();
};

export const getCountries = async () => {
  const response = await fetch('/api/countries');
  if (!response.ok) {
    throw new Error('Failed to fetch countries');
  }
  return response.json();
};
