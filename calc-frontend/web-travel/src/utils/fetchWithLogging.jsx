export async function fetchWithLogging(url, options = {}) {
  // Функция для очистки строки от переносов и невидимых символов
  const cleanString = (str) => str.replace(/[\r\n]+/g, '').trim();

  // Очистка URL
  const cleanUrl = cleanString(url);

  // Клонируем и очищаем заголовки
  const cleanHeaders = {};
  if (options.headers) {
    for (const [key, value] of Object.entries(options.headers)) {
      cleanHeaders[key] = cleanString(value);
    }
  }

  // Очистка тела, если это строка
  let cleanBody = options.body;
  if (typeof cleanBody === 'string') {
    cleanBody = cleanString(cleanBody);
  }

  const cleanOptions = {
    ...options,
    headers: cleanHeaders,
    body: cleanBody,
  };

  console.log('--- FETCH REQUEST ---');
  console.log('URL:', cleanUrl);
  console.log('Options:', cleanOptions);

  const response = await fetch(cleanUrl, cleanOptions);

  console.log('--- FETCH RESPONSE ---');
  console.log('Status:', response.status);
  console.log('Headers:', [...response.headers.entries()]);

  return response;
}
