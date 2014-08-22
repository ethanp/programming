publicController = {}

# home page '/'
publicController.index = (req, res) ->
    res.render 'public/index'

publicController.about = (req, res) ->
  res.render 'public/about'

publicController.go = (req, res) ->
  console.log 'voilá'
  res.render 'public/about'

module.exports = publicController
