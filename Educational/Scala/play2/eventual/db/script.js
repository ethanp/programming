/*
 * Author: Sari Haj Hussein
 */

use eventual

db.dropDatabase()

db.createCollection('celebrities')

db.celebrities.insert({
	name: {first: 'Ann-Margret', last: 'Olsson'},
	website: 'http://www.ann-margret.com',
	bio: ''
})

db.celebrities.insert({
	name: {first: 'Ingrid', last: 'Bergman'},
	website: 'http://www.ingridbergman.com',
	bio: ''
})

db.celebrities.insert({
	name: {first: 'Greta', last: 'Garbo'},
	website: 'http://www.gretagarbo.com',
	bio: ''
})

db.celebrities.insert({
	name: {first: 'Noomi', last: 'Rapace'},
	website: 'http://www.noomi-rapace.info',
	bio: ''
})

db.celebrities.insert({
	name: {first: 'Mirja', last: 'Turestedt'},
	website: 'http://www.mirjaturestedt.com',
	bio: ''
})